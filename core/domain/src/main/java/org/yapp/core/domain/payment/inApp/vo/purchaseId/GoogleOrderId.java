package org.yapp.core.domain.payment.inApp.vo.purchaseId;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public record GoogleOrderId(String value) implements StorePurchaseId {

    public GoogleOrderId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("purchaseToken empty");
        }
    }

    @Override
    public InAppStore store() {
        return InAppStore.PLAY_STORE;
    }
}
