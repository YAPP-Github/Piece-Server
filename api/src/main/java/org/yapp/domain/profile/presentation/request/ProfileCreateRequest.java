package org.yapp.domain.profile.presentation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ProfileCreateRequest(

    @NotBlank(message = "닉네임은 필수입니다.")
    String nickname,

    @Past(message = "생년월일은 현재 날짜보다 과거여야 합니다.")
    LocalDate birthdate,

    @Positive(message = "키는 0보다 커야 합니다.")
    Integer height,

    String job,

    String location,

    @Pattern(regexp = "^(흡연|비흡연)$", message = "흡연 여부는 '흡연' 또는 '비흡연'이어야 합니다.")
    String smokingStatus,

    String religion,

    @Pattern(regexp = "^(활동|은둔)$", message = "SNS 활동 수준은 '활동', '은둔' 중 하나여야 합니다.")
    String snsActivityLevel,

    Map<String, String> contacts,

    @Pattern(regexp = "\\d{10,11}", message = "유효한 핸드폰 번호를 입력해야 합니다.")
    String phoneNumber,

    @Pattern(regexp = "^https?://.*", message = "이미지 URL은 유효한 형식이어야 합니다.")
    String imageUrl,

    @NotEmpty(message = "최소 하나 이상의 가치관 Pick을 선택해야 합니다.")
    List<@Valid ValuePickRequest> valuePicks,

    @NotEmpty(message = "최소 하나 이상의 가치관 Talk을 작성해야 합니다.")
    List<@Valid ValueTalkRequest> valueTalks

) {

    public record ValuePickRequest(
        @NotNull(message = "ValuePick ID는 필수입니다.")
        Long valuePickId,

        @NotNull(message = "선택된 답변은 필수입니다.")
        @Positive(message = "선택된 답변은 1 이상이어야 합니다.")
        Integer selectedAnswer
    ) {

    }

    public record ValueTalkRequest(
        @NotNull(message = "ValueTalk ID는 필수입니다.")
        Long valueTalkId,

        @Size(max = 300, message = "답변은 최대 300자까지 작성 가능합니다.")
        String answer
    ) {

    }
}
