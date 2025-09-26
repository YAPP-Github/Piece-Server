package org.yapp.unit.domain.auth.application.oauth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.yapp.domain.auth.application.oauth.OauthProvider;
import org.yapp.domain.auth.application.oauth.OauthProviderResolver;

@ExtendWith(MockitoExtension.class)
class OauthProviderResolverTest {

    @Mock
    private OauthProvider googleProvider;
    @Mock
    private OauthProvider kakaoProvider;

    @InjectMocks
    private OauthProviderResolver oauthProviderResolver;

    @Test
    @DisplayName("구글 Provider 를 찾아 반환한다")
    void testFindGoogleProvider_Success() {
        //given
        String providerName = "google";
        when(googleProvider.match(providerName)).thenReturn(true);
        when(kakaoProvider.match(providerName)).thenReturn(false);
        List<OauthProvider> oauthProviders = List.of(kakaoProvider, googleProvider);
        ReflectionTestUtils.setField(oauthProviderResolver, "oauthProviders", oauthProviders);

        //when
        OauthProvider foundProvider = oauthProviderResolver.find(providerName);

        //then
        assertThat(foundProvider).isEqualTo(googleProvider);
    }

    @Test
    @DisplayName("존재하지 않는 Provider 요청에 실패한다")
    void testFindInvalidProvider_Failure() {
        // given
        String providerName = "nonexistent";
        when(googleProvider.match(providerName)).thenReturn(false);
        when(kakaoProvider.match(providerName)).thenReturn(false);

        List<OauthProvider> oauthProviders = List.of(googleProvider, kakaoProvider);
        ReflectionTestUtils.setField(oauthProviderResolver, "oauthProviders", oauthProviders);

        // When & Then
        assertThrows(RuntimeException.class, () -> oauthProviderResolver.find(providerName));
    }

    @Test
    @DisplayName("카카오 Provider 를 찾아 반환한다")
    void testFindKakaoProvider_Success() {
        // given
        String providerName = "kakao";
        when(kakaoProvider.match(providerName)).thenReturn(true);
        when(googleProvider.match(providerName)).thenReturn(false);

        List<OauthProvider> oauthProviders = List.of(googleProvider, kakaoProvider);
        ReflectionTestUtils.setField(oauthProviderResolver, "oauthProviders", oauthProviders);

        // When
        OauthProvider foundProvider = oauthProviderResolver.find(providerName);

        // Then
        assertThat(foundProvider).isEqualTo(kakaoProvider);
    }
}