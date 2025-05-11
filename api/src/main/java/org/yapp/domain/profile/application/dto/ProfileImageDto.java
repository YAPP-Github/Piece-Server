package org.yapp.domain.profile.application.dto;

import org.yapp.core.domain.profile.ProfileImage;
import org.yapp.core.domain.profile.ProfileImageStatus;

public record ProfileImageDto(
        Long profileId,
        Long profileImageId,
        String imageUrl,
        ProfileImageStatus status) {

    public static ProfileImageDto from(ProfileImage profileImage) {
        return new ProfileImageDto(profileImage.getProfile().getId(), profileImage.getId(),
                profileImage.getImageUrl(), profileImage.getStatus());
    }

}