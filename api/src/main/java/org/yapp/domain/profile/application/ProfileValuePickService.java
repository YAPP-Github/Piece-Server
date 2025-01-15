package org.yapp.domain.profile.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValuePickRepository;
import org.yapp.domain.profile.presentation.request.ProfileValuePickCreateRequest;
import org.yapp.domain.value.ValuePick;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileValuePickService {

  private final ProfileRepository profileRepository;
  private final ProfileValuePickRepository profileValueRepository;

  @Transactional
  public List<ProfileValuePick> createAllProfileValues(Long profileId,
      List<ProfileValuePickCreateRequest> createRequests) {
    Profile profile = profileRepository.findById(profileId)
                                       .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

    List<ProfileValuePick> profileValuePicks = createRequests.stream()
                                                             .map(request -> createProfileValuePick(profile,
                                                                 ValuePick.builder().id(request.valuePickId()).build(),
                                                                 request.selectedAnswer()))
                                                             .toList();

    profileValueRepository.saveAll(profileValuePicks);
    return profileValuePicks;
  }

  @Transactional(readOnly = true)
  public List<ProfileValuePick> getAllProfileValuesByProfileId(Long profileId) {
    return profileValueRepository.findByProfileIdOrderByValuePickId(profileId);
  }

  private ProfileValuePick createProfileValuePick(Profile profile, ValuePick valuePick, Integer answer) {
    return ProfileValuePick.builder().profile(profile).valuePick(valuePick).selectedAnswer(answer).build();
  }
}
