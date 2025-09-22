package org.yapp.core.domain.payment.inApp.enums;

import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.AppleReceipt;
import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.GooglePurchaseToken;
import org.yapp.core.domain.payment.inApp.vo.purchaseCredential.StorePurchaseCredential;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.AppleTransactionId;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.GoogleOrderId;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.StorePurchaseId;

public enum InAppStore {
    PLAY_STORE {
        @Override
        public StorePurchaseId createPurchaseId(String purchaseId) {
            return new GoogleOrderId(purchaseId);
        }

        @Override
        public StorePurchaseCredential createPurchaseCredential(String purchaseCredential) {
            return new GooglePurchaseToken(purchaseCredential);
        }
    },
    APP_STORE {
        @Override
        public StorePurchaseId createPurchaseId(String purchaseId) {
            return new AppleTransactionId(purchaseId);
        }

        @Override
        public StorePurchaseCredential createPurchaseCredential(String purchaseCredential) {
            return new AppleReceipt(purchaseCredential);
        }
    };

    public abstract StorePurchaseId createPurchaseId(String purchaseId);

    public abstract StorePurchaseCredential createPurchaseCredential(String purchaseCredential);
}
