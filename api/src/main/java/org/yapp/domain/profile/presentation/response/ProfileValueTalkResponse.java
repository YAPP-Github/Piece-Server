package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.ProfileValueTalk;

public record ProfileValueTalkResponse(
    String title,
    String category,
    Long profileValueTalkId,
    String answer,
    String summary
) {

    public ProfileValueTalkResponse(ProfileValueTalk profileValueTalk) {
        this(
            profileValueTalk.getValueTalk().getTitle(),
            profileValueTalk.getValueTalk().getCategory(),
            profileValueTalk.getId(),
            profileValueTalk.getAnswer(),
            profileValueTalk.getSummary()
        );
    }
}
