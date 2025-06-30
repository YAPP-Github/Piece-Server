package org.yapp.match.dto.response;

import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.user.User;

public record ManualMatchCandidateResponse(Long userId, String nickName) {

    public static ManualMatchCandidateResponse of(User user) {
        Profile profile = user.getProfile();
        ProfileBasic profileBasic = profile.getProfileBasic();
        return new ManualMatchCandidateResponse(user.getId(), profileBasic.getNickname());
    }
}
