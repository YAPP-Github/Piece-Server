package org.yapp.core.domain.payment.inApp.vo.purchaseCredential;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public record AppleReceipt(String value) implements StorePurchaseCredential {

    @Override
    public InAppStore store() {
        return InAppStore.APP_STORE;
    }
}
