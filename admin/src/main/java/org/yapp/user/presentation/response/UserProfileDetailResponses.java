package org.yapp.user.presentation.response;

import java.util.List;
import org.yapp.core.domain.profile.ProfileValueTalk;

public record UserProfileDetailResponses(String nickname, String description, String imageUrl,
                                         List<UserProfileDetailResponse> responses) {

    public static UserProfileDetailResponses from(String nickname, String description,
        String imageUrl,
        List<ProfileValueTalk> profileValueTalks) {
        List<UserProfileDetailResponse> profileValueTalkResponses = profileValueTalks.stream()
            .map(UserProfileDetailResponse::new).toList();

        return new UserProfileDetailResponses(nickname, description, imageUrl,
            profileValueTalkResponses);
    }
}
