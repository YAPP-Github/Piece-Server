package org.yapp.domain.profile.presentation.response;

import java.util.List;
import org.yapp.domain.profile.ProfileValueTalk;

public record ProfileValueTalkResponses(List<ProfileValueTalkResponse> responses) {

    public static ProfileValueTalkResponses from(List<ProfileValueTalk> profileValueTalks) {
        List<ProfileValueTalkResponse> profileValueTalkResponses = profileValueTalks.stream()
            .map(ProfileValueTalkResponse::new).toList();

        return new ProfileValueTalkResponses(profileValueTalkResponses);
    }
}
