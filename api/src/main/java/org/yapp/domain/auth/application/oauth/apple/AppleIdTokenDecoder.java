package org.yapp.domain.auth.application.oauth.apple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class AppleIdTokenDecoder {

    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    public Claims decodeIdToken(String idToken) {
        try {
            JsonNode appleKeys = fetchApplePublicKeys();
            String kid = extractKidFromIdToken(idToken);

            JsonNode matchedKey = findMatchingKey(appleKeys, kid);
            if (matchedKey == null) {
                throw new RuntimeException("No matching key found for kid: " + kid);
            }

            PublicKey publicKey = buildPublicKey(matchedKey);

            Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(idToken);

            return claimsJws.getBody(); // Payload 반환
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode idToken", e);
        }
    }

    private JsonNode fetchApplePublicKeys() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url = new URL(APPLE_KEYS_URL);
        return objectMapper.readTree(url).get("keys");
    }

    private String extractKidFromIdToken(String idToken) throws JsonProcessingException {
        String[] parts = idToken.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid idToken format");
        }
        String header = new String(Base64.getDecoder().decode(parts[0]));
        JsonNode headerJson = new ObjectMapper().readTree(header);
        return headerJson.get("kid").asText();
    }

    private JsonNode findMatchingKey(JsonNode appleKeys, String kid) {
        for (JsonNode key : appleKeys) {
            if (key.get("kid").asText().equals(kid)) {
                return key;
            }
        }
        return null;
    }

    private PublicKey buildPublicKey(JsonNode keyInfo) throws Exception {
        String n = keyInfo.get("n").asText();
        String e = keyInfo.get("e").asText();

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicKeySpec);
    }
}