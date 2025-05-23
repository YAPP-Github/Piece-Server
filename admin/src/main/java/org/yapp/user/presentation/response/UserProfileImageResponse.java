package org.yapp.user.presentation.response;

import org.yapp.core.domain.profile.ProfileImageStatus;

record UserProfileImageResponse(Long profileImageId,
                                String profileImageUrl,
                                ProfileImageStatus profileImageStatus) {

}