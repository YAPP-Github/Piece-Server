package org.yapp.domain.profile.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.ContactType;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.profile.ProfileStatus;
import org.yapp.core.domain.profile.ProfileValuePick;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.RoleStatus;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.ProfileErrorCode;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.presentation.request.ProfileBasicUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileCreateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValuePickUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValuePickUpdateRequest.ProfileValuePickPair;
import org.yapp.domain.profile.presentation.request.ProfileValueTalkUpdateRequest;
import org.yapp.domain.profile.presentation.request.ProfileValueTalkUpdateRequest.ProfileValueTalkPair;
import org.yapp.domain.user.application.UserService;

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
            .description(dto.description())
            .birthdate(dto.birthdate())
            .height(dto.height())
            .job(dto.job())
            .location(dto.location())
            .smokingStatus(dto.smokingStatus())
            .weight(dto.weight())
            .snsActivityLevel(dto.snsActivityLevel())
            .contacts(dto.contacts().entrySet().stream().collect(Collectors.toMap(
                entry -> ContactType.valueOf(entry.getKey()),
                Map.Entry::getValue
            )))
            .imageUrl(dto.imageUrl())
            .build();

        Profile profile = Profile.builder().profileBasic(profileBasic)
            .build();

        profileRepository.save(profile);

        List<ProfileValuePick> allProfileValues = profileValuePickService.createAllProfileValuePicks(
            profile.getId(), dto.valuePicks());

        List<ProfileValueTalk> allProfileTalks = profileValueTalkService.createAllProfileValues(
            profile.getId(), dto.valueTalks()
        );

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
        return profileRepository.findByProfileBasic_LocationAndUser_Role(locationName,
            RoleStatus.USER.getStatus());
    }

    @Transactional
    public Profile updateProfileBasic(long userId, ProfileBasicUpdateRequest dto) {
        User user = this.userService.getUserById(userId);
        Profile profile = getProfileById(user.getProfile().getId());

        ProfileBasic profileBasic = dto.toProfileBasic();

        profile.updateBasic(profileBasic);
        updateProfileStatus(profile);

        return profile;
    }

    @Transactional
    public Profile updateProfileValuePicks(long userId, ProfileValuePickUpdateRequest dto) {
        User user = this.userService.getUserById(userId);
        Profile profile = getProfileById(user.getProfile().getId());

        List<ProfileValuePick> profileValuePicks = profile.getProfileValuePicks();

        Map<Long, Integer> userProfileValuePickIdMaps = new HashMap<>();
        for (ProfileValuePickPair profileValuePickPair : dto.profileValuePickUpdateRequests()) {
            final Long profileValuePickId = profileValuePickPair.profileValuePickId();
            final Integer selectedAnswer = profileValuePickPair.selectedAnswer();

            userProfileValuePickIdMaps.put(profileValuePickId, selectedAnswer);
        }

        for (ProfileValuePick profileValuePick : profileValuePicks) {
            Long profileValuePickId = profileValuePick.getId();

            if (userProfileValuePickIdMaps.containsKey(profileValuePickId)) {
                Integer selectedAnswer = userProfileValuePickIdMaps.get(profileValuePickId);
                profileValuePick.updatedSelectedAnswer(selectedAnswer);
            }
        }

        updateProfileStatus(profile);
        return profile;
    }

    @Transactional
    public Profile updateProfileValueTalks(long userId, ProfileValueTalkUpdateRequest dto) {
        User user = this.userService.getUserById(userId);
        Profile profile = getProfileById(user.getProfile().getId());

        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        HashMap<Long, ProfileValueTalk> profileValueTalkHashMap = profileValueTalks.stream()
            .collect(Collectors.toMap(
                ProfileValueTalk::getId,
                profileValueTalk -> profileValueTalk,
                (existing, replacement) -> existing,
                HashMap::new
            ));

        for (ProfileValueTalkPair profileValuePickPair : dto.profileValueTalkUpdateRequests()) {
            final Long profileValueTalkId = profileValuePickPair.profileValueTalkId();
            final String answer = profileValuePickPair.answer();
            final String summary = profileValuePickPair.summary();

            ProfileValueTalk profileValueTalk = profileValueTalkHashMap.get(profileValueTalkId);
            if (profileValueTalk != null) {
                profileValueTalk.updateAnswer(answer);
                profileValueTalk.updateSummary(summary);
            }
        }

        updateProfileStatus(profile);
        return profile;
    }

    private void updateProfileStatus(Profile profile) {
        ProfileStatus profileStatus = profile.getProfileStatus();

        if (ProfileStatus.INCOMPLETE.equals(profileStatus) ||
            ProfileStatus.REJECTED.equals(profileStatus)) {
            profile.updateProfileStatus(ProfileStatus.REVISED);
        }
    }

    public boolean isNicknameAvailable(String nickname) {
        return !profileRepository.existsByProfileBasic_Nickname(nickname);
    }
}
