package org.yapp.domain.match.application.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.match.application.algorithm.MatchingAlgorithm;
import org.yapp.domain.profile.Profile;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.profile.enums.Location;

@Component
@RequiredArgsConstructor
public class PreferenceBasedMatcher implements CoupleMatcher {

  private final MatchingAlgorithm matchingAlgorithm;
  private final ProfileService profileService;

  @Override
  public void match() {
    List<Profile> unmatchedProfiles = new ArrayList<>();

    // 지역별로 나누기
    List<String> locationNames = Arrays.stream(Location.values()).map(Location::getName).toList();

    //지역별로 매칭
    for (String locationName : locationNames) {
      List<Profile> profiles = profileService.getValidProfilesByLocation(locationName);
      List<Profile> remains = matchingAlgorithm.doMatch(profiles);
      unmatchedProfiles.addAll(remains);
    }

    // 매칭 안되고 남은 애들 매칭하기
    List<Profile> finalRemains = matchingAlgorithm.doMatch(unmatchedProfiles);
    //TODO : 매칭 끝끝내 안된 사람들 처리 방식 논의 필요
  }
}
