package org.yapp.domain.profile.presentation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProfileValuePickCreateRequest(
    @NotNull(message = "ValuePick ID는 필수입니다.")
    Long valuePickId,

    @NotNull(message = "선택된 답변은 필수입니다.")
    @Positive(message = "선택된 답변은 1 이상이어야 합니다.")
    Integer selectedAnswer
) {

}