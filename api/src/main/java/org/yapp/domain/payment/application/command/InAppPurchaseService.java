package org.yapp.domain.payment.application.command;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.StorePurchaseCredential;
import org.yapp.infra.billing.common.InAppPurchaseVerifier;
import org.yapp.infra.billing.common.VerificationRequest;
import org.yapp.infra.billing.common.VerificationResult;

@Slf4j
@Service
public class InAppPurchaseService {

    private final InAppPurchaseRewardService rewardService;
    private final Map<InAppStore, InAppPurchaseVerifier> verifiers;

    public InAppPurchaseService(InAppPurchaseRewardService rewardService,
        List<InAppPurchaseVerifier> verifiers) {
        this.rewardService = rewardService;
        this.verifiers = verifiers.stream()
            .collect(Collectors.toMap(InAppPurchaseVerifier::getStore,
                Function.identity()));
    }

    public void execute(Long userId, String productId,
        StorePurchaseCredential storePurchaseCredential) {

        var verificationRequestDto = new VerificationRequest(productId, storePurchaseCredential);
        var verificationResult = this.verifyPurchase(verificationRequestDto);

        this.rewardWithRetry(userId, verificationResult);
    }

    private VerificationResult verifyPurchase(VerificationRequest verificationRequestDto) {
        final InAppStore store = verificationRequestDto.purchaseCredential().store();

        InAppPurchaseVerifier verifier = this.verifiers.get(store);

        if (verifier == null) {
            throw new IllegalArgumentException("Invalid store: " + store);
        }

        VerificationResult result = verifier.verify(verificationRequestDto);

        return result;
    }


    public boolean rewardWithRetry(Long userId, VerificationResult verificationResult) {
        return rewardService.execute(userId, verificationResult);
    }

}
