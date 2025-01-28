package org.yapp.user.presentation.response;

import org.yapp.domain.profile.ProfileValueTalk;

public record UserProfileDetailResponse(
    String category,
    String title,
    String answer
) {

    public UserProfileDetailResponse(ProfileValueTalk profileValueTalk) {
        this(
            profileValueTalk.getValueTalk().getTitle(),
            profileValueTalk.getValueTalk().getCategory(),
            profileValueTalk.getAnswer()
        );
    }
}
