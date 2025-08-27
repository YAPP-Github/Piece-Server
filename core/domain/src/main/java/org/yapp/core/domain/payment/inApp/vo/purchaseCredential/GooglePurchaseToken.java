package org.yapp.core.domain.payment.inApp.vo.purchaseCredential;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

public record GooglePurchaseToken(String value) implements StorePurchaseCredential {

    @Override
    public InAppStore store() {
        return InAppStore.PLAY_STORE;
    }
}
