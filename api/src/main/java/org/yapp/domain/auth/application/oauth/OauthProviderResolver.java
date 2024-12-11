package org.yapp.domain.auth.application.oauth;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthProviderResolver {
    private final List<OauthProvider> oauthProviders;
    public OauthProvider find(String providerName){
        return oauthProviders.stream()
                .filter(provider -> provider.match(providerName))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }
}
