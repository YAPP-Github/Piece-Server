package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.application.util.DateUtils;

public record ProfileBasicResponse(String nickname, String description, int age, String birthdate,
                                   int height,
                                   String job,
                                   String location,
                                   String smokingStatus, Integer weight, String snsActivityLevel,
                                   String imageUrl) {

    public static ProfileBasicResponse from(Profile profile) {

        ProfileBasic basic = profile.getProfileBasic();

        return new ProfileBasicResponse(basic.getNickname(),
            basic.getDescription(),
            DateUtils.calculateAge(basic.getBirthdate()),
            basic.getBirthdate().toString(), basic.getHeight(),
            basic.getJob(), basic.getLocation(), basic.getSmokingStatus(), basic.getWeight(),
            basic.getSnsActivityLevel(),
            basic.getImageUrl());
    }
}