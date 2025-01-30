package org.yapp.domain.profile.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.core.domain.value.ValueTalk;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValueTalkRepository;
import org.yapp.domain.profile.presentation.request.ProfileValueTalkCreateRequest;
import org.yapp.domain.profile.presentation.response.ProfileValueTalkResponses;
import org.yapp.domain.user.application.UserService;
import org.yapp.domain.value.application.ValueTalkService;
import org.yapp.exception.ApplicationException;
import org.yapp.exception.error.code.ProfileErrorCode;

@Service
@RequiredArgsConstructor
public class ProfileValueTalkService {

    private final ProfileRepository profileRepository;
    private final ProfileValueTalkRepository profileValueTalkRepository;
    private final UserService userService;
    private final ValueTalkService valueTalkService;

    @Transactional
    public List<ProfileValueTalk> createAllProfileValues(Long profileId,
        List<ProfileValueTalkCreateRequest> createRequests) {
        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

        List<ProfileValueTalk> profileValueTalks = createRequests.stream()
            .map(request -> createProfileValueTalk(profile,
                ValueTalk.builder().id(request.valueTalkId()).build(), request.answer()))
            .toList();

        profileValueTalkRepository.saveAll(profileValueTalks);
        return profileValueTalks;
    }

    private ProfileValueTalk createProfileValueTalk(Profile profile, ValueTalk valueTalk,
        String answer) {
        return ProfileValueTalk.builder()
            .profile(profile)
            .valueTalk(valueTalk)
            .answer(answer)
            .build();
    }

    @Transactional(readOnly = true)
    public ProfileValueTalkResponses getProfileValueTalkResponses(Long userId) {
        User user = userService.getUserById(userId);

        List<ProfileValueTalk> activeProfileValueTalks = profileValueTalkRepository.findActiveProfileValueTalksByProfileId(
            user.getProfile().getId());

        return ProfileValueTalkResponses.from(activeProfileValueTalks);
    }
}
