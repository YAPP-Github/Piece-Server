package org.yapp.domain.profile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValueRepository;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.application.ValuePickService;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileValueService {
    private final ProfileRepository profileRepository;
    private final ValuePickService valuePickService;
    private final ProfileValueRepository profileValueRepository;

    @Transactional
    public  List<ProfileValuePick> createAllProfileValues(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

        List<ValuePick> allValuePicks = valuePickService.getAllValueItems();
        List<ProfileValuePick> profileValuePicks = allValuePicks.stream()
                .map(valueItem -> createProfileValue(profile, valueItem))
                .toList();

        profileValueRepository.saveAll(profileValuePicks);
        return profileValuePicks;
    }

    private ProfileValuePick createProfileValue(Profile profile, ValuePick valuePick) {
        return ProfileValuePick.builder()
                .profile(profile)
                .valuePick(valuePick)
                .selectedAnswer(null)
                .build();
    }
}
