package org.yapp.core.domain.payment.inApp.vo.purchaseCredential;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public sealed interface StorePurchaseCredential permits GooglePurchaseToken, AppleReceipt {

    String value();

    InAppStore store();
}