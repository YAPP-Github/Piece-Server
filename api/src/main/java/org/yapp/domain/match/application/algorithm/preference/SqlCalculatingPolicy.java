package org.yapp.domain.match.application.algorithm.preference;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.domain.profile.application.ProfileValuePickService;

@Component
@RequiredArgsConstructor
public class SqlCalculatingPolicy implements PreferenceCalculatingPolicy {

  private final ProfileValuePickService profileValuePickService;
  
  @Override
  public int calculatePreference(Long profile1Id, Long profile2Id) {
    return profileValuePickService.getWeightWithSql(profile1Id, profile2Id);
  }
}
