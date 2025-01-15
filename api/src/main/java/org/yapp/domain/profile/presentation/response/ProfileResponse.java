package org.yapp.domain.profile.presentation.response;

import java.util.List;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;

public record ProfileResponse(Long id, String nickname, String birthdate, int height, String job,
                              String location,
                              String smokingStatus, String religion, String snsActivityLevel,
                              String imageUrl,
                              List<ProfileValueTalkResponse> profileTalks,
                              List<ProfileValuePickResponse> profilePicks) {

    public static ProfileResponse from(Profile profile) {
        ProfileBasic basic = profile.getProfileBasic();

        List<ProfileValuePickResponse> picks = profile.getProfileValuePicks().stream().map(
            ProfileValuePickResponse::new).toList();

        List<ProfileValueTalkResponse> talks = profile.getProfileValueTalks().stream().map(
            ProfileValueTalkResponse::new).toList();

        return new ProfileResponse(profile.getId(), basic.getNickname(),
            basic.getBirthdate().toString(), basic.getHeight(),
            basic.getJob(), basic.getLocation(), basic.getSmokingStatus(), basic.getReligion(),
            basic.getSnsActivityLevel(),
            basic.getImageUrl(), talks, picks);
    }
}