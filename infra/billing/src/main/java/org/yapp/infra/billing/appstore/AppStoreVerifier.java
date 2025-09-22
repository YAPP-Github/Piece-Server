package org.yapp.infra.billing.appstore;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.core.domain.payment.inApp.vo.purchaseId.AppleTransactionId;
import org.yapp.infra.billing.common.InAppPurchaseVerifier;
import org.yapp.infra.billing.common.VerificationRequest;
import org.yapp.infra.billing.common.VerificationResult;

@Slf4j
@Component
public class AppStoreVerifier implements InAppPurchaseVerifier {

    private final String bundleId;
    private final ApplePublicKeyProvider applePublicKeyProvider;

    public AppStoreVerifier(
        @Value("${apple.app-store.bundle-id}") String bundleId,
        ApplePublicKeyProvider applePublicKeyProvider
    ) {
        this.bundleId = bundleId;
        this.applePublicKeyProvider = applePublicKeyProvider;
    }

    @Override
    public VerificationResult verify(VerificationRequest request) {
        final String receipt = request.purchaseCredential().value();
        String keyId = this.loadKeyId(receipt);

        try {
            Jws<Claims> jwsClaims = this.parseReceipt(receipt);
            return verifyPayload(request, jwsClaims.getPayload());
        } catch (SignatureException e) {
            log.warn(
                "App Store verification failed with stale key (kid={}). Evicting and retrying.",
                keyId, e);

            try {
                applePublicKeyProvider.evictKey(keyId);
                Jws<Claims> jwsClaims = parseReceipt(receipt);
                return verifyPayload(request, jwsClaims.getPayload());
            } catch (Exception retryException) {
                log.error("Failed to verify App Store purchase even after retry for kid: {}",
                    keyId, retryException);
                return VerificationResult.failure(null, request.purchaseCredential(),
                    request.productId());
            }

        } catch (Exception e) {
            log.error("Error verifying App Store purchase for transaction: {}", receipt, e);
            return VerificationResult.failure(null, request.purchaseCredential(),
                request.productId());
        }
    }

    private Jws<Claims> parseReceipt(String receipt) {
        var keyLocator = this.getKeyLocator();

        return Jwts.parser()
            .keyLocator(keyLocator)
            .build()
            .parseSignedClaims(receipt);
    }

    private VerificationResult verifyPayload(VerificationRequest request,
        Claims payload) {
        String BUNDLE_ID_NAME = "bundleId";
        String PRODUCT_ID_NAME = "productId";
        String TRANSACTION_ID_NAME = "transactionId";

        String payloadBundleId = payload.get(BUNDLE_ID_NAME, String.class);
        if (!this.bundleId.equals(payloadBundleId)) {
            log.warn(
                "App Store verification failed: Bundle ID mismatch. expected={}, actual={}",
                this.bundleId, payloadBundleId);
            return VerificationResult.failure(null, request.purchaseCredential(),
                request.productId());
        }

        Date expirationDate = payload.getExpiration();
        if (expirationDate != null && expirationDate.before(new Date())) {
            log.warn("App Store verification failed: Transaction expired.");
            return VerificationResult.failure(null, request.purchaseCredential(),
                request.productId());
        }

        String productId = payload.get(PRODUCT_ID_NAME, String.class);
        if (!request.productId().equals(productId)) {
            log.warn(
                "App Store verification failed: Product ID mismatch. expected={}, actual={}",
                request.productId(), productId);
            return VerificationResult.failure(null, request.purchaseCredential(),
                request.productId());
        }

        String transactionId = payload.get(TRANSACTION_ID_NAME, String.class);

        log.info("App Store purchase verification successful for transactionId: {}",
            transactionId);
        return VerificationResult.success(
            new AppleTransactionId(transactionId),
            request.purchaseCredential(),
            productId
        );
    }

    private String loadKeyId(String receipt) {
        String unsignedJwt = receipt.substring(0, receipt.lastIndexOf('.'));

        var untrustedJws = Jwts.parser()
            .build().parseUnsecuredClaims(unsignedJwt);

        String keyId = (String) untrustedJws.getHeader().get("keyId");

        if (keyId == null) {
            throw new IllegalArgumentException("JWS header does not contain 'kid' claim.");
        }

        return keyId;
    }

    private Locator<Key> getKeyLocator() {
        return (header) -> {

            if (!(header instanceof JwsHeader)) {
                throw new IllegalArgumentException(
                    "Unsupported header type: " + header.getClass().getSimpleName());
            }

            JwsHeader jwsHeader = (JwsHeader) header;
            String kid = jwsHeader.getKeyId();

            if (kid == null) {
                throw new IllegalArgumentException("JWS header does not contain 'kid' claim.");
            }

            return applePublicKeyProvider.getPublicKey(kid);
        };

    }


    @Override
    public InAppStore getStore() {
        return InAppStore.APP_STORE;
    }
}