package org.yapp.domain.match.application.algorithm.preference;

import java.time.LocalDate;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.domain.profile.application.ProfileService;

@RequiredArgsConstructor
public class AgePreferenceCalculatingPolicy implements PreferenceCalculatingPolicy {

    private static final int AGE_GAP = 4;
    private static final int MAX_WEIGHT = 10;
    private final ProfileService profileService;

    @Override
    public int calculatePreference(Long profile1Id, Long profile2Id) {
        Profile profile1 = profileService.getProfileById(profile1Id);
        Profile profile2 = profileService.getProfileById(profile2Id);

        ProfileBasic profileBasic1 = profile1.getProfileBasic();
        LocalDate birthDate1 = profileBasic1.getBirthdate();

        ProfileBasic profileBasic2 = profile2.getProfileBasic();
        LocalDate birthDate2 = profileBasic2.getBirthdate();

        return calculateAgeWeight(birthDate1, birthDate2);
    }

    private int calculateAgeWeight(LocalDate birthDate1, LocalDate birthDate2) {
        int ageDifference = Period.between(birthDate1, birthDate2).getYears();
        int ageDifferenceAbs = Math.abs(ageDifference);
        if (ageDifferenceAbs >= MAX_WEIGHT * 2) {
            return -MAX_WEIGHT;
        }
        return MAX_WEIGHT - (ageDifferenceAbs / AGE_GAP) * AGE_GAP;
    }
}
