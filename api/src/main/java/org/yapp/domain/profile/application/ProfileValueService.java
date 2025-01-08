package org.yapp.domain.profile.application;

import java.util.List;
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

@Service
@RequiredArgsConstructor
public class ProfileValueService {

    private final ProfileRepository profileRepository;
    private final ValuePickService valuePickService;
    private final ProfileValueRepository profileValueRepository;

    @Transactional
    public List<ProfileValuePick> createAllProfileValues(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

        List<ValuePick> allValuePicks = valuePickService.getAllActiveValuePicks();
        List<ProfileValuePick> profileValuePicks = allValuePicks.stream()
            .map(valuePick -> createProfileValuePick(profile, valuePick, 0))
            .toList();

        profileValueRepository.saveAll(profileValuePicks);
        return profileValuePicks;
    }

    private ProfileValuePick createProfileValuePick(Profile profile, ValuePick valuePick,
        Integer answer) {
        return ProfileValuePick.builder()
            .profile(profile)
            .valuePick(valuePick)
            .selectedAnswer(answer)
            .build();
    }
}
