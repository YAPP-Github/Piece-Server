package org.yapp.user.presentation.response;

import org.yapp.core.domain.profile.ProfileImage;

public record UserProfileImageDetailResponse(String profileImageUrl,
                                             UserProfileImageResponse pendingProfileImage) {

    public static UserProfileImageDetailResponse from(ProfileImage profileImage,
        String profileImageUrl) {
        if (profileImage == null) {
            return new UserProfileImageDetailResponse(profileImageUrl, null);
        }

        UserProfileImageResponse innerResponse = new UserProfileImageResponse(
            profileImage.getId(), profileImage.getImageUrl(), profileImage.getStatus());

        return new UserProfileImageDetailResponse(profileImageUrl, innerResponse);
    }
}
