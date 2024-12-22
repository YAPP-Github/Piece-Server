package org.yapp.domain.profile.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileBasic;
import org.yapp.domain.profile.ProfileBio;
import org.yapp.domain.profile.application.dto.ProfileCreateDto;
import org.yapp.domain.profile.dao.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final ProfileRepository profileRepository;

  @Transactional
  public Profile create(ProfileCreateDto dto) {
    ProfileBasic profileBasic =
        ProfileBasic.builder().nickname(dto.phoneNumber()).phoneNumber(dto.phoneNumber()).build();
    ProfileBio profileBio = ProfileBio.builder().build();

    Profile profile = Profile.builder().profileBasic(profileBasic).profileBio(profileBio).build();

    return profileRepository.save(profile);
  }
}
