package org.yapp.infra.billing.common;

import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.StorePurchaseCredential;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.StorePurchaseId;

public record VerificationResult(
    boolean isSuccess,
    StorePurchaseId purchaseId,
    StorePurchaseCredential purchaseCredential,
    String productId) {

    public static VerificationResult success(StorePurchaseId purchaseId,
        StorePurchaseCredential purchaseCredential, String productId) {
        return new VerificationResult(true, purchaseId, purchaseCredential, productId);
    }

    public static VerificationResult failure(StorePurchaseId purchaseId,
        StorePurchaseCredential purchaseCredential, String productId) {
        return new VerificationResult(false, purchaseId, purchaseCredential, productId);
    }
}
