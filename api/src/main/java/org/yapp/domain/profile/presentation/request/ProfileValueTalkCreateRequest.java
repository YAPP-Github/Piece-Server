package org.yapp.domain.profile.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProfileValueTalkCreateRequest(
    @NotNull(message = "ValueTalk ID는 필수입니다.")
    Long valueTalkId,

    @Size(max = 300, message = "답변은 최대 300자까지 작성 가능합니다.")
    String answer
) {

}