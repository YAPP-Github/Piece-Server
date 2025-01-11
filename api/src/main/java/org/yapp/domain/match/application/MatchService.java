package org.yapp.domain.match.application;

import org.springframework.stereotype.Service;
import org.yapp.application.AuthenticationService;
import org.yapp.domain.match.MatchInfo;
import org.yapp.domain.match.dao.MatchInfoRepository;
import org.yapp.error.dto.MatchErrorCode;
import org.yapp.error.exception.ApplicationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {
  private final MatchInfoRepository matchInfoRepository;
  private final AuthenticationService authenticationService;

  public LocalDate getMatchDate() {
    LocalDateTime nowDateTime = LocalDateTime.now();
    LocalDate nowDate = nowDateTime.toLocalDate();
    LocalTime cutOffTime = LocalTime.of(22, 0);

    if (nowDateTime.toLocalTime().isBefore(cutOffTime)) {
      return nowDate.minusDays(1);
    }
    return nowDate;
  }

  public MatchInfo getMatchInfo() {
    Long userId = authenticationService.getUserId();
    return matchInfoRepository.findByUserIdAndDate(userId, getMatchDate())
                              .orElseThrow(() -> new ApplicationException(MatchErrorCode.NOTFOUND_MATCH));
  }
}
