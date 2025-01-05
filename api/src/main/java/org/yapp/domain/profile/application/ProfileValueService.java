package org.yapp.domain.profile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.ProfileValueItem;
import org.yapp.domain.profile.dao.ProfileRepository;
import org.yapp.domain.profile.dao.ProfileValueRepository;
import org.yapp.domain.value.ValueItem;
import org.yapp.domain.value.application.ValueItemService;
import org.yapp.error.dto.ProfileErrorCode;
import org.yapp.error.exception.ApplicationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileValueService {
    private final ProfileRepository profileRepository;
    private final ValueItemService valueItemService;
    private final ProfileValueRepository profileValueRepository;

    @Transactional
    public  List<ProfileValueItem> createAllProfileValues(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ApplicationException(ProfileErrorCode.NOTFOUND_PROFILE));

        List<ValueItem> allValueItems = valueItemService.getAllValueItems();
        List<ProfileValueItem> profileValueItems = allValueItems.stream()
                .map(valueItem -> createProfileValue(profile, valueItem))
                .toList();

        profileValueRepository.saveAll(profileValueItems);
        return profileValueItems;
    }

    private ProfileValueItem createProfileValue(Profile profile, ValueItem valueItem) {
        return ProfileValueItem.builder()
                .profile(profile)
                .valueItem(valueItem)
                .selectedAnswer(null)
                .build();
    }
}
