package org.yapp.domain.payment.application.command;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.common.vo.Puzzle;
import org.yapp.core.domain.payment.common.enums.PaymentStatus;
import org.yapp.core.domain.payment.inApp.entity.InAppPayment;
import org.yapp.core.domain.product.CashProduct;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.CashProductErrorCode;
import org.yapp.domain.cashProduct.dao.CashProductRepository;
import org.yapp.domain.payment.dao.InAppPaymentRepository;
import org.yapp.domain.user.application.UserService;
import org.yapp.infra.billing.common.VerificationResult;

@Service
@RequiredArgsConstructor
@Slf4j
public class InAppPurchaseRewardService {

    private final InAppPaymentRepository inAppPaymentRepository;
    private final CashProductRepository cashProductRepository;
    private final UserService userService;

    @Transactional
    public boolean execute(Long userId, VerificationResult verificationResult) {
        String purchaseId = verificationResult.purchaseId().value();
        String productUUID = verificationResult.productId();

        var inAppPaymentOptional = inAppPaymentRepository.findByPurchaseId(purchaseId);

        if (!isValidInAppPaymentStatus(inAppPaymentOptional)) {
            return false;
        }

        CashProduct cashProduct = cashProductRepository.findByUuidAndIsActive(productUUID, true)
            .orElseThrow(() -> new ApplicationException(CashProductErrorCode.PRODUCT_NOT_FOUND));

        var rewardPuzzle = cashProduct.getRewardPuzzle();
        grantPuzzleToUser(userId, rewardPuzzle);

        completeInAppPayment(inAppPaymentOptional.get());
        return true;
    }


    /**
     * Optional<InAppPayment>을 전달받고, 보상을 진행할 수 있는 건인지 검증합니다. 검증 조건: InAppPayment의 상태가 PENDING
     * 상태이어야합니다.
     *
     * @param inAppPaymentOptional
     * @return
     */
    private boolean isValidInAppPaymentStatus(Optional<InAppPayment> inAppPaymentOptional) {
        if (inAppPaymentOptional.isPresent()) {
            InAppPayment payment = inAppPaymentOptional.get();

            return payment.getPaymentStatus() == PaymentStatus.PENDING;
        }
        return false;
    }

    private void grantPuzzleToUser(Long userId, Puzzle rewardPuzzle) {
        var user = userService.getUserById(userId);
        user.addPuzzle(rewardPuzzle);
    }

    private void completeInAppPayment(InAppPayment inAppPayment) {
        inAppPayment.complete();
    }
}
