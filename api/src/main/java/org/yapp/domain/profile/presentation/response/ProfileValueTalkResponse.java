package org.yapp.domain.profile.presentation.response;

import java.util.List;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.value.ValueTalk;

public record ProfileValueTalkResponse(
    String title,
    String category,
    Long valueTalkId,
    Long profileValueTalkId,
    String answer,
    String summary,
    String placeholder,
    List<String> guides
) {

    public static ProfileValueTalkResponse from(ProfileValueTalk profileValueTalk) {
        ValueTalk valueTalk = profileValueTalk.getValueTalk();

        return new ProfileValueTalkResponse(
            valueTalk.getTitle(),
            valueTalk.getCategory(),
            valueTalk.getId(),
            profileValueTalk.getId(),
            profileValueTalk.getAnswer(),
            profileValueTalk.getSummary(),
            valueTalk.getPlaceholder(),
            valueTalk.getGuides()
        );
    }
}
