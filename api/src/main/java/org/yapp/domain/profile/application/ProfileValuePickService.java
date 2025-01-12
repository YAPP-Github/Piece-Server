package org.yapp.domain.profile.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileValuePick;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValuePickRepository;
import org.yapp.domain.profile.presentation.request.ProfileValuePickCreateRequest;
import org.yapp.domain.profile.presentation.response.ProfileValuePickResponses;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.application.ValuePickService;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;

@Service
@RequiredArgsConstructor
public class ProfileValuePickService {

    private final UserService userService;
    private final ValuePickService valuePickService;
    private final ProfileRepository profileRepository;
    private final ProfileValuePickRepository profileValuePickRepository;


    @Transactional
    public List<ProfileValuePick> createAllProfileValues(Long profileId,
        List<ProfileValuePickCreateRequest> createRequests) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

        List<ProfileValuePick> profileValuePicks = createRequests.stream()
            .map(request -> createProfileValuePick(profile,
                ValuePick.builder().id(request.valuePickId()).build(), request.selectedAnswer()))
            .toList();

        profileValuePickRepository.saveAll(profileValuePicks);
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

    @Transactional(readOnly = true)
    public ProfileValuePickResponses getProfileValuePickResponses(Long userId) {
        User user = userService.getUserById(userId);

        List<ValuePick> activeValuePicks = valuePickService.getAllActiveValuePicks();

        Map<Long, ProfileValuePick> userProfileValuePicks = profileValuePickRepository.findByProfileId(
                user.getProfile().getId())
            .stream()
            .collect(Collectors.toMap(
                profileValuePick -> profileValuePick.getValuePick().getId(),
                profileValuePick -> profileValuePick
            ));

        return ProfileValuePickResponses.from(activeValuePicks, userProfileValuePicks);
    }
}
