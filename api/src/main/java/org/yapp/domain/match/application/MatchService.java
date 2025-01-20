package org.yapp.domain.match.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.application.AuthenticationService;
import org.yapp.domain.match.MatchInfo;
import org.yapp.domain.match.dao.MatchInfoRepository;
import org.yapp.domain.user.User;
import org.yapp.domain.user.application.UserService;
import org.yapp.error.dto.MatchErrorCode;
import org.yapp.error.exception.ApplicationException;

@Service
@RequiredArgsConstructor
public class MatchService {

  private final MatchInfoRepository matchInfoRepository;
  private final AuthenticationService authenticationService;
  private final UserService userService;

  public MatchInfo createMatchInfo(Long user1Id, Long user2Id) {
    User user1 = userService.getUserById(user1Id);
    User user2 = userService.getUserById(user2Id);
    return matchInfoRepository.save(new MatchInfo(LocalDate.now(), user1, user2));
  }

  public LocalDate getMatchDate() {
    LocalDateTime nowDateTime = LocalDateTime.now();
    LocalDate nowDate = nowDateTime.toLocalDate();
    LocalTime cutOffTime = LocalTime.of(22, 0);

    if (nowDateTime.toLocalTime().isBefore(cutOffTime)) {
      return nowDate.minusDays(1);
    }
    return nowDate;
  }

  public boolean wasUsersMatched(Long user1Id, Long user2Id) {
    Optional<MatchInfo> matchInfoByIds = matchInfoRepository.findMatchInfoByIds(user1Id, user2Id);
    return matchInfoByIds.isPresent();
  }

  public MatchInfo getMatchInfo() {
    Long userId = authenticationService.getUserId();
    return matchInfoRepository.findByUserIdAndDate(userId, getMatchDate())
        .orElseThrow(() -> new ApplicationException(MatchErrorCode.NOTFOUND_MATCH));
  }
}
