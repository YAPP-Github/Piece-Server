package org.yapp.infra.billing.playstore;

import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.GoogleOrderId;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.StorePurchaseId;
import org.yapp.infra.billing.common.InAppPurchaseVerifier;
import org.yapp.infra.billing.common.VerificationRequest;
import org.yapp.infra.billing.common.VerificationResult;
import org.yapp.infra.billing.playstore.enums.PurchaseState;

@Slf4j
@Component
public class PlayStoreVerifier implements InAppPurchaseVerifier {

    private final AndroidPublisher androidPublisher;

    private final String packageName;

    public PlayStoreVerifier(AndroidPublisher androidPublisher,
        @Value("${google.play-store.package-name}") String packageName) {
        this.androidPublisher = androidPublisher;
        this.packageName = packageName;
    }

    @Override
    public VerificationResult verify(VerificationRequest request) {
        final String purchaseToken = request.purchaseCredential().value();

        try {
            ProductPurchase purchase = androidPublisher.purchases().products()
                .get(packageName, request.productId(), purchaseToken)
                .execute();

            StorePurchaseId purchaseId = new GoogleOrderId(purchase.getOrderId());
            if (purchase.getPurchaseState() == PurchaseState.PURCHASED.getValue()) {
                log.info("Play Store purchase verification successful for orderId: {}",
                    purchase.getOrderId());

                return VerificationResult.success(
                    purchaseId,
                    request.purchaseCredential(),
                    purchase.getProductId());
            } else {
                PurchaseState state = PurchaseState.fromValue(purchase.getPurchaseState());

                log.warn(
                    "Play Store purchase verification failed for token: {}. Purchase state: {}",
                    purchaseToken, state);

                return VerificationResult.failure(purchaseId, request.purchaseCredential(),
                    purchase.getProductId());
            }
        } catch (IOException e) {
            log.error("Error verifying Play Store purchase for token: " + purchaseToken,
                e);
            return VerificationResult.failure(null, request.purchaseCredential(),
                null);
        }
    }

    @Override
    public InAppStore getStore() {
        return InAppStore.PLAY_STORE;
    }
}
