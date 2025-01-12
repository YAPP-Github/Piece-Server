package org.yapp.domain.match.application.matcher;

import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.algorithm.PreferenceBasedMatchingAlgorithm;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.profile.enums.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PreferenceBasedMatcher implements CoupleMatcher {
  private final PreferenceBasedMatchingAlgorithm matchingAlgorithm;
  private final ProfileService profileService;

  @Override
  public void match() {
    List<Profile> unmatchedProfiles = new ArrayList<>();

    // 지역별로 나누기
    List<String> locationNames = Arrays.stream(Location.values()).map(Location::getName).toList();

    //지역별로 매칭
    for (String locationName : locationNames) {
      List<Profile> profiles = profileService.getProfilesByLocation(locationName);
      List<Profile> remains = matchingAlgorithm.doMatch(profiles);
      unmatchedProfiles.addAll(remains);
    }

    // 매칭 안되고 남은 애들 매칭하기
    matchingAlgorithm.doMatch(unmatchedProfiles);
  }
}
