package org.yapp.infra.billing.common;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public interface InAppPurchaseVerifier {

    VerificationResult verify(VerificationRequest request);

    InAppStore getStore();
}
