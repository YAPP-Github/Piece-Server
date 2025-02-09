package org.yapp.domain.profile.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import org.yapp.core.domain.profile.ContactType;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.domain.profile.presentation.validation.ValidContactType;

public record ProfileBasicUpdateRequest(@NotBlank(message = "닉네임은 비어있을 수 없습니다.") String nickname,

                                        @NotBlank(message = "나를 소개하는 한 마디는 비어있을 수 없습니다") String description,

                                        @NotBlank(message = "생일은 비어있을 수 없습니다.") String birthdate,

                                        @Min(value = 50, message = "키는 최소 50cm 이상이어야 합니다.") @Max(value = 300,
                                            message = "키는 250cm를 초과할 수 없습니다.") int height,

                                        @NotBlank(message = "직업은 비어있을 수 없습니다.") String job,

                                        @NotBlank(message = "위치는 비어있을 수 없습니다.") String location,

                                        @NotBlank(message = "흡연 정보는 비어있을 수 없습니다")
                                        String smokingStatus,

                                        @NotBlank(message = "SNS 활동 정보는 비어있을 수 없습니다.")
                                        String snsActivityLevel,

                                        @Min(value = 1, message = "몸무게는 최소 1kg 이상이어야 합니다.")
                                        Integer weight,

                                        @Pattern(regexp = "^\\d{10,11}$",
                                            message = "전화번호는 10자리에서 11자리 숫자여야 합니다.") String phoneNumber,

                                        @Pattern(regexp = "^https?://.*", message = "이미지 URL은 유효한 형식이어야 합니다.")
                                        String imageUrl,

                                        @Schema(description =
                                            "연락처 정보 (키: ContactType, 값: 연락처 정보) - 사용 가능한 키: " +
                                                "[KAKAO_TALK_ID(필수), OPEN_CHAT_URL(선택), INSTAGRAM_ID(선택), PHONE_NUMBER(선택)]",
                                            example = "{\"KAKAO_TALK_ID\": \"john_kakao\", \"PHONE_NUMBER\": \"01098765432\"}")
                                        @ValidContactType
                                        Map<String, String> contacts) {

    public ProfileBasic toProfileBasic() {

        return ProfileBasic.builder()
            .nickname(nickname)
            .description(description)
            .birthdate(LocalDate.parse(birthdate))
            .height(height)
            .job(job)
            .location(location)
            .smokingStatus(smokingStatus)
            .weight(weight)
            .snsActivityLevel(snsActivityLevel)
            .imageUrl(imageUrl)
            .contacts(contacts.entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> ContactType.valueOf(entry.getKey()),
                    Map.Entry::getValue
                ))
            )
            .build();
    }

}