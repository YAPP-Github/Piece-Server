package org.yapp.core.domain.payment.inApp.vo.purchaseId;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public sealed interface StorePurchaseId permits GoogleOrderId, AppleTransactionId {

    String value();

    InAppStore store();
}
