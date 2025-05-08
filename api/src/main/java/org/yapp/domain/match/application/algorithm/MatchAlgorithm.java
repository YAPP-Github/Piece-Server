package org.yapp.domain.match.application.algorithm;

import java.util.List;
import org.yapp.core.domain.profile.Profile;

public interface MatchAlgorithm {

  /**
   * 가치관 기반으로 매칭을 수행
   *
   * @param profiles 매칭 수행할 프로필 리스트
   * @return 매칭이 이루어지지 않은 프로필 리스트
   */
  public List<Profile> doMatch(List<Profile> profiles);
}
