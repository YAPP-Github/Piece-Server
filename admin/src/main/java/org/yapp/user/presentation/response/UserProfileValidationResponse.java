package org.yapp.user.presentation.response;

import java.time.LocalDate;
import lombok.Builder;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.user.User;

@Builder
public record UserProfileValidationResponse(Long userId, String description,
                                            String nickname,
                                            String name, LocalDate birthdate, String phoneNumber,
                                            LocalDate joinDate,
                                            String profileStatus, boolean rejectImage,
                                            boolean rejectDescription) {

    public static UserProfileValidationResponse from(User user, boolean rejectImage,
        boolean rejectDescription) {
        Profile profile = user.getProfile();

        return UserProfileValidationResponse.builder()
            .userId(user.getId())
            .description(profile != null ? profile.getProfileBasic().getDescription() : null)
            .nickname(profile != null ? profile.getProfileBasic().getNickname() : null)
            .name(user.getName())
            .birthdate(profile != null ? profile.getProfileBasic().getBirthdate() : null)
            .phoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : null)
            .joinDate(user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : null)
            .profileStatus(profile != null ? profile.getProfileStatus().getDisplayName() : null)
            .rejectImage(rejectImage)
            .rejectDescription(rejectDescription)
            .build();
    }
}
