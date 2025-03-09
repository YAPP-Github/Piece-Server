package org.yapp.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.apple")
public class AppleOauthProperties {

    private String grantType;
    private String clientId;
    private String keyId;
    private String teamId;
    private String authServer;
    private String privateKey;
}