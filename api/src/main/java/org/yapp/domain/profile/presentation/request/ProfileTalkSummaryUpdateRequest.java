package org.yapp.domain.profile.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileTalkSummaryUpdateRequest(
    @NotBlank(message = "요약 내용은 필수 입력 사항입니다.") @Size(max = 20, message = "요약 내용은 최대 20자까지 입력할 수 있습니다.") String summary) {

}
