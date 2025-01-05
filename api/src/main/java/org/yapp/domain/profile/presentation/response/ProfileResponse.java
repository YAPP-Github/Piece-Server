package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;

import java.util.List;

public record ProfileResponse(Long id, String nickname, String birthdate, int height, String job, String location,
                              String smokingStatus, String religion, String snsActivityLevel, String phoneNumber,
                              String imageUrl, String introduction, String goal, String interest,
                              List<ProfileValueItemResponse> profileValues) {

  public static ProfileResponse from(Profile profile) {
    ProfileBasic basic = profile.getProfileBasic();
    ProfileBio bio = profile.getProfileBio();

    List<ProfileValueItemResponse> values = profile.getProfileValueItems().stream().map(ProfileValueItemResponse::new).toList();

    return new ProfileResponse(profile.getId(), basic.getNickname(), basic.getBirthdate().toString(), basic.getHeight(),
        basic.getJob(), basic.getLocation(), basic.getSmokingStatus(), basic.getReligion(), basic.getSnsActivityLevel(),
        basic.getPhoneNumber(), basic.getImageUrl(), bio.getIntroduction(), bio.getGoal(), bio.getInterest(), values);
  }
}