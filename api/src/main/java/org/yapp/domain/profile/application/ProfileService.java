package org.yapp.domain.profile.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.auth.presentation.dto.enums.RoleStatus;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.ProfileValueTalk;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.presentation.request.ProfileCreateRequest;
import org.yapp.domain.profile.presentation.request.ProfileUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValuePickPair;
import org.yapp.domain.profile.presentation.request.ProfileValuePickUpdateRequest;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserService userService;
  private final ProfileValuePickService profileValuePickService;
  private final ProfileValueTalkService profileValueTalkService;
  private final ProfileRepository profileRepository;

  @Transactional
  public Profile create(ProfileCreateRequest dto) {
    ProfileBasic profileBasic = ProfileBasic.builder()
                                            .nickname(dto.nickname())
                                            .birthdate(dto.birthdate())
                                            .height(dto.height())
                                            .job(dto.job())
                                            .location(dto.location())
                                            .smokingStatus(dto.smokingStatus())
                                            .religion(dto.religion())
                                            .snsActivityLevel(dto.snsActivityLevel())
                                            .contacts(dto.contacts())
                                            .imageUrl(dto.imageUrl())
                                            .build();

    Profile profile = Profile.builder().profileBasic(profileBasic).build();

    profileRepository.save(profile);

    List<ProfileValuePick> allProfileValues =
        profileValuePickService.createAllProfileValues(profile.getId(), dto.valuePicks());

    List<ProfileValueTalk> allProfileTalks =
        profileValueTalkService.createAllProfileValues(profile.getId(), dto.valueTalks());

    profile.updateProfileValuePicks(allProfileValues);
    profile.updateProfileValueTalks(allProfileTalks);

    return profile;
  }

  @Transactional(readOnly = true)
  public Profile getProfileById(long profileId) {
    return profileRepository.findById(profileId)
                            .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));
  }

  @Transactional(readOnly = true)
  public List<Profile> getValidProfilesByLocation(String locationName) {
    return profileRepository.findByProfileBasic_LocationAndUser_Role(locationName, RoleStatus.USER.getStatus());
  }

  public boolean isNicknameAvailable(String nickname) {
    return !profileRepository.existsByProfileBasic_Nickname(nickname);
  }

  @Transactional
  public Profile updateByUserId(long userId, ProfileUpdateRequest dto) {
    User user = this.userService.getUserById(userId);
    Profile profile = getProfileById(user.getProfile().getId());

    ProfileBasic profileBasic = dto.toProfileBasic();
    ProfileBio profileBio = dto.toProfileBio();

    profile.updateBasic(profileBasic);
    profile.updateBio(profileBio);

    return profile;
  }

  @Transactional
  public Profile updateProfileValues(long userId, ProfileValuePickUpdateRequest dto) {
    User user = this.userService.getUserById(userId);
    Profile profile = getProfileById(user.getProfile().getId());

    List<ProfileValuePick> profileValuePicks = profile.getProfileValuePicks();

    Map<Long, Integer> userValueItemAnswerMap = new HashMap<>();
    for (ProfileValuePickPair profileValuePickPair : dto.profileValuePickPairs()) {
      final Long valueItemId = profileValuePickPair.valueItemId();
      final Integer selectedAnswer = profileValuePickPair.selectedAnswer();

      userValueItemAnswerMap.put(valueItemId, selectedAnswer);
    }

    for (ProfileValuePick profileValuePick : profileValuePicks) {
      final Long profileValueItemID = profileValuePick.getValuePick().getId();
      profileValuePick.updatedSelectedAnswer(userValueItemAnswerMap.get(profileValueItemID));
    }

    return profile;
  }
}
