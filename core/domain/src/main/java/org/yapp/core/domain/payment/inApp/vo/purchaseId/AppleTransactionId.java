package org.yapp.core.domain.payment.inApp.vo.purchaseId;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public record AppleTransactionId(String value) implements StorePurchaseId {

    public AppleTransactionId {
        if (!value.matches("\\d{10,20}")) {
            throw new IllegalArgumentException("transactionId format");
        }
    }

    @Override
    public InAppStore store() {
        return InAppStore.APP_STORE;
    }
}
