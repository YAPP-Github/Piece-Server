package org.yapp.domain.profile.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;
import org.yapp.domain.profile.ProfileValue;
import org.yapp.domain.profile.presentation.request.ProfileUpdateRequest;
import org.yapp.domain.profile.application.dto.ProfileCreateDto;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.presentation.request.ProfileValuePair;
import org.yapp.domain.profile.presentation.request.ProfileValueUpdateRequest;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final UserService userService;
  private final ProfileValueService profileValueService;
  private final ProfileRepository profileRepository;

  @Transactional
  public Profile create(ProfileCreateDto dto) {
    ProfileBasic profileBasic = ProfileBasic.builder().nickname(dto.nickName()).phoneNumber(dto.phoneNumber()).build();
    ProfileBio profileBio = ProfileBio.builder().build();

    Profile profile = Profile.builder().profileBasic(profileBasic).profileBio(profileBio).build();
    Profile savedProfile = profileRepository.save(profile);
    List<ProfileValue> allProfileValues = profileValueService.createAllProfileValues(savedProfile.getId());
    savedProfile.updateProfileValues(allProfileValues);

    return savedProfile;
  }

  @Transactional
  public Profile getProfileById(long profileId) {
    return profileRepository.findById(profileId)
                            .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));
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
  public Profile updateProfileValues(long userId, ProfileValueUpdateRequest dto) {
    User user = this.userService.getUserById(userId);
    Profile profile = getProfileById(user.getProfile().getId());

    List<ProfileValue> profileValues = profile.getProfileValues();

    Map<Long, Integer> userValueItemAnswerMap = new HashMap<>();
    for (ProfileValuePair profileValuePair : dto.profileValuePairs()) {
      final Long valueItemId = profileValuePair.valueItemId();
      final Integer selectedAnswer = profileValuePair.selectedAnswer();

      userValueItemAnswerMap.put(valueItemId, selectedAnswer);
    }

    for (ProfileValue profileValue : profileValues) {
      final Long profileValueItemID = profileValue.getValueItem().getId();
      profileValue.updatedSelectedAnswer(userValueItemAnswerMap.get(profileValueItemID));
    }

    return profile;
  }

  public boolean isNicknameAvailable(String nickname) {
    return !profileRepository.existsByProfileBasic_Nickname(nickname);
  }
}
