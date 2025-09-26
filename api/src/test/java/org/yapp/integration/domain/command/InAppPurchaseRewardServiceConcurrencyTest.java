package org.yapp.integration.domain.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.util.ReflectionTestUtils;
import org.yapp.core.domain.common.vo.Money;
import org.yapp.core.domain.common.vo.Puzzle;
import org.yapp.core.domain.payment.common.enums.PaymentStatus;
import org.yapp.core.domain.payment.inApp.entity.InAppPayment;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.core.domain.product.CashProduct;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.user.UserPuzzleWallet;
import org.yapp.domain.cashProduct.dao.CashProductRepository;
import org.yapp.domain.payment.application.command.InAppPurchaseRewardService;
import org.yapp.domain.payment.dao.InAppPaymentRepository;
import org.yapp.domain.user.dao.UserRepository;
import org.yapp.infra.billing.common.VerificationResult;

@SpringBootTest
@Slf4j
class InAppPurchaseRewardServiceConcurrencyTest {

    @Autowired
    private InAppPurchaseRewardService rewardService;

    @Autowired
    private InAppPaymentRepository paymentRepository;

    @Autowired
    private CashProductRepository cashProductRepository;

    @Autowired
    private UserRepository userRepository;

    private Long userId;
    private VerificationResult verification;
    private Puzzle rewardPuzzle;

    @BeforeEach
    void setUp() {
        UserPuzzleWallet wallet = new UserPuzzleWallet();

        User user = User.builder()
            .name("tester")
            .puzzleWallet(wallet)
            .build();

        userRepository.save(user);

        this.userId = user.getId();

        String productUuid = UUID.randomUUID().toString();
        rewardPuzzle = Puzzle.of(5L);

        CashProduct product = BeanUtils.instantiateClass(CashProduct.class);
        ReflectionTestUtils.setField(product, "uuid", productUuid);
        ReflectionTestUtils.setField(product, "name", "Test Cash Pack");
        ReflectionTestUtils.setField(product, "price", Money.of(new BigDecimal("1000"), "KRW"));
        ReflectionTestUtils.setField(product, "discountRate", BigDecimal.ZERO);
        ReflectionTestUtils.setField(product, "isActive", true);
        ReflectionTestUtils.setField(product, "rewardPuzzle", rewardPuzzle);
        cashProductRepository.save(product);

        String purchaseId = "ORDER-" + UUID.randomUUID().toString();
        String purchaseCredential = "TOKEN-" + UUID.randomUUID().toString();

        InAppPayment payment = InAppPayment.builder()
            .userId(userId)
            .purchaseId(purchaseId)
            .purchaseCredential(purchaseCredential)
            .inAppStore(InAppStore.PLAY_STORE)
            .productUUID(productUuid)
            .status(PaymentStatus.PENDING).build();

        paymentRepository.save(payment);

        verification = VerificationResult.success(InAppStore.PLAY_STORE.createPurchaseId(purchaseId)
            , InAppStore.PLAY_STORE.createPurchaseCredential(purchaseCredential)
            , productUuid);
    }

    @Test
    @DisplayName("execute() should be idempotent under concurrent calls")
    void execute_isIdempotent_underConcurrency() throws Exception {
        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);

        AtomicInteger rewardSkippedCount = new AtomicInteger(0);
        AtomicInteger optimisticExceptionCount = new AtomicInteger(0);

        Runnable worker = () -> {
            try {
                latch.await();
                var ret = rewardService.execute(userId, verification);
                if (!ret) {
                    rewardSkippedCount.incrementAndGet();
                }
            } catch (ObjectOptimisticLockingFailureException e) {
                log.error("낙관적 락 충돌 발생");
                optimisticExceptionCount.incrementAndGet();
            } catch (Exception e) {
                log.error("알 수 없는 예외 발생", e);
            }
        };

        for (int i = 0; i < threads; i++) {
            pool.submit(worker);
        }

        latch.countDown();
        pool.shutdown();
        pool.awaitTermination(100, TimeUnit.SECONDS);

        User updated = userRepository.findById(userId).orElseThrow();
        assertThat(updated.getPuzzleWallet().getPuzzleCount()).isEqualTo(rewardPuzzle.getCount());

        assertThat(optimisticExceptionCount.get() + rewardSkippedCount.get()).isEqualTo(
            threads - 1);
    }
}