package org.yapp.domain.match.application.matcher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.profile.Profile;
import org.yapp.domain.match.application.algorithm.MatchingAlgorithm;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.profile.enums.Location;

@Component
@RequiredArgsConstructor
public class PreferenceBasedMatcher implements CoupleMatcher {

  private final MatchingAlgorithm matchingAlgorithm;
  private final ProfileService profileService;

  @Override
  public void match() {
    List<Profile> unmatchedProfiles;
    ExecutorService executor = Executors.newFixedThreadPool(10);
    try {
      // 지역별로 나누기
      List<String> locationNames = Arrays.stream(Location.values()).map(Location::getName)
          .toList();

      //지역별로 매칭
      List<CompletableFuture<List<Profile>>> futures = locationNames.stream()
          .map(location -> CompletableFuture.supplyAsync(() -> {
            List<Profile> profiles = profileService.getValidProfilesByLocation(
                location);
            return matchingAlgorithm.doMatch(profiles);
          }, executor)).toList();

      unmatchedProfiles = futures.stream()
          .map(CompletableFuture::join)
          .flatMap(List::stream)
          .toList();

      // 매칭 안되고 남은 애들 매칭하기
      matchingAlgorithm.doMatch(unmatchedProfiles);
    } finally {
      executor.shutdown();
    }
  }
}
