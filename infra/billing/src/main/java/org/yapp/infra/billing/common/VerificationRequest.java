package org.yapp.infra.billing.common;

import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.StorePurchaseCredential;

public record VerificationRequest(
    String productId,
    StorePurchaseCredential purchaseCredential
) {

}