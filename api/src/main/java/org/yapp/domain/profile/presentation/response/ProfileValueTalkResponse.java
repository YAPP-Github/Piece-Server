package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.ProfileValueTalk;

public record ProfileValueTalkResponse(
    Long id,
    Long valueTalkId,
    String title,
    String category,
    String answer,
    String summary,
    String guide
) {

    public ProfileValueTalkResponse(ProfileValueTalk profileValueTalk) {
        this(
            profileValueTalk.getId(),
            profileValueTalk.getValueTalk().getId(),
            profileValueTalk.getValueTalk().getTitle(),
            profileValueTalk.getValueTalk().getCategory(),
            profileValueTalk.getAnswer(),
            profileValueTalk.getSummary(),
            profileValueTalk.getValueTalk().getGuide()
        );
    }
}
