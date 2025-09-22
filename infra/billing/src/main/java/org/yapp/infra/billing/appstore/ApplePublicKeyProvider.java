package org.yapp.infra.billing.appstore;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import java.security.PublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class ApplePublicKeyProvider {

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    @Cacheable(value = "applePublicKeys")
    public PublicKey getPublicKey(String keyId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String jsonString = restTemplate.getForObject(APPLE_KEYS_URL, String.class);

            JWKSet jwkSet = JWKSet.parse(jsonString);

            JWK jwk = jwkSet.getKeyByKeyId(keyId);
            if (jwk != null) {
                return jwk.toRSAKey().toPublicKey();
            }
        } catch (Exception e) {
            log.error("Error getting Apple public key for kid: {}", keyId, e);
        }
        throw new RuntimeException("Failed to get Apple public key for kid: " + keyId);
    }

    @CacheEvict(value = "applePublicKeys")
    public void evictKey(String keyId) {
        log.info("Evicting Apple public key for kid: {}", keyId);
    }
}