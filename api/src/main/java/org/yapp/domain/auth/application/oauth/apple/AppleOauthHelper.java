package org.yapp.domain.auth.application.oauth.apple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;
import org.yapp.global.config.AppleOauthProperties;

@Component
@RequiredArgsConstructor
public class AppleOauthHelper {

    private final AppleOauthProperties appleProperties;
    private final AppleIdTokenDecoder appleIdTokenDecoder;

    public String generateClientSecret() {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
            .claim("iss", appleProperties.getTeamId())
            .claim("aud", appleProperties.getAuthServer())
            .claim("sub", appleProperties.getClientId())
            .claim("exp", Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
            .claim("iat", new Date())
            .header()
            .add("kid", appleProperties.getKeyId())
            .and()
            .signWith(getPrivateKey())
            .compact();
    }

    public Claims decodeIdToken(String idToken) {
        return this.appleIdTokenDecoder.decodeIdToken(idToken);
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleProperties.getPrivateKey());

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }
}