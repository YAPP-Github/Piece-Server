package org.yapp.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OauthUserDeleteRequest {

    @NotNull(message = "providerName은 null일 수 없습니다.")
    @Pattern(
        regexp = "^(apple|kakao|google)$",
        message = "providerName은 apple, kakao, google 중 하나여야 합니다."
    )
    private String providerName;
    private String oauthCredential;
    @Size(max = 100, message = "탈퇴 이유는 100자 이하여야합니다.")
    private String reason;
}
