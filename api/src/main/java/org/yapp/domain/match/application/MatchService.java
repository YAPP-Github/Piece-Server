package org.yapp.domain.match.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.auth.AuthenticationService;
import org.yapp.core.domain.match.MatchInfo;
import org.yapp.core.domain.match.enums.MatchStatus;
import org.yapp.core.domain.profile.ContactType;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.profile.ProfileValuePick;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.MatchErrorCode;
import org.yapp.domain.match.dao.MatchInfoRepository;
import org.yapp.domain.match.presentation.dto.response.MatchInfoResponse;
import org.yapp.domain.match.presentation.dto.response.MatchProfileBasicResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValuePickInnerResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValuePickResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValueTalkInnerResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValueTalkResponse;
import org.yapp.domain.profile.application.ProfileValuePickService;
import org.yapp.domain.user.application.UserService;

@Service
@RequiredArgsConstructor
public class MatchService {

  private final MatchInfoRepository matchInfoRepository;
  private final AuthenticationService authenticationService;
  private final ProfileValuePickService profileValuePickService;
  private final UserService userService;

  @Transactional
  public MatchInfo createMatchInfo(Long user1Id, Long user2Id) {
    User user1 = userService.getUserById(user1Id);
    User user2 = userService.getUserById(user2Id);
    return matchInfoRepository.save(new MatchInfo(LocalDate.now(), user1, user2));
  }

  @Transactional(readOnly = true)
  public MatchProfileBasicResponse getMatchProfileBasic() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);

    User matchedUser = getMatchedUser(userId, matchInfo);
    Profile matchedProfile = matchedUser.getProfile();
    return MatchProfileBasicResponse.fromProfile(matchInfo.getId(), matchedProfile);
  }

  @Transactional
  public void checkPiece() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);
    matchInfo.checkPiece(userId);
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

  @Transactional(readOnly = true)
  public boolean wasUsersMatched(Long user1Id, Long user2Id) {
    Optional<MatchInfo> matchInfoByIds = matchInfoRepository.findMatchInfoByIds(user1Id,
        user2Id);
    return matchInfoByIds.isPresent();
  }

  @Transactional(readOnly = true)
  public MatchInfo getMatchInfo(Long userId) {
    return matchInfoRepository.findByUserIdAndDate(userId, getMatchDate())
        .orElseThrow(() -> new ApplicationException(MatchErrorCode.NOTFOUND_MATCH));
  }

  @Transactional(readOnly = true)
  public MatchInfoResponse getMatchInfoResponse() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);

    User matchedUser = getMatchedUser(userId, matchInfo);
    User user = userService.getUserById(userId);
    List<String> matchedValues = getMatchedValues(user.getProfile().getId(),
        matchedUser.getProfile().getId());

    ProfileBasic profileBasic = matchedUser.getProfile().getProfileBasic();

    return MatchInfoResponse.builder()
        .matchId(matchInfo.getId())
        .matchStatus(getMatchStatus(userId, matchInfo))
        .description(profileBasic
            .getDescription())
        .nickname(profileBasic.getNickname())
        .birthYear(
            String.valueOf(profileBasic.getBirthdate().getYear()).substring(2))
        .location(profileBasic.getLocation())
        .job(profileBasic.getJob())
        .matchedValueCount(matchedValues.size())
        .matchedValueList(matchedValues)
        .build();
  }

  private User getMatchedUser(Long userId, MatchInfo matchInfo) {
    if (userId.equals(matchInfo.getUser1().getId())) {
      return matchInfo.getUser2();
    }
    return matchInfo.getUser1();
  }

  private String getMatchStatus(Long userId, MatchInfo matchInfo) {
    boolean isUser1 = userId.equals(matchInfo.getUser1().getId());

    boolean userPieceChecked =
        isUser1 ? matchInfo.getUser1PieceChecked() : matchInfo.getUser2PieceChecked();
    boolean userAccepted = isUser1 ? matchInfo.getUser1Accepted() : matchInfo.getUser2Accepted();
    boolean otherAccepted = isUser1 ? matchInfo.getUser2Accepted() : matchInfo.getUser1Accepted();
    boolean userRefused = isUser1 ? matchInfo.getUser1Refused() : matchInfo.getUser2Refused();

    if (!userPieceChecked) {
      return MatchStatus.BEFORE_OPEN.getStatus();
    }
    if (userRefused) {
      return MatchStatus.REFUSED.getStatus();
    }
    if (userAccepted && otherAccepted) {
      return MatchStatus.MATCHED.getStatus();
    }
    if (userAccepted) {
      return MatchStatus.RESPONDED.getStatus();
    }
    if (otherAccepted) {
      return MatchStatus.GREEN_LIGHT.getStatus();
    }
    return MatchStatus.WAITING.getStatus();
  }

  @Transactional(readOnly = true)
  public MatchValueTalkResponse getMatchValueTalk() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);
    User matchedUser = getMatchedUser(userId, matchInfo);
    List<ProfileValueTalk> profileValueTalks = matchedUser.getProfile().getProfileValueTalks();
    List<MatchValueTalkInnerResponse> talkResponses = new ArrayList<>();
    for (ProfileValueTalk profileValueTalk : profileValueTalks) {
      String summary = profileValueTalk.getSummary();
      String answer = profileValueTalk.getAnswer();
      String category = profileValueTalk.getValueTalk().getCategory();
      talkResponses.add(new MatchValueTalkInnerResponse(category, summary, answer));
    }
    return new MatchValueTalkResponse(matchInfo.getId(), "",
        matchedUser.getProfile().getProfileBasic().getNickname(), talkResponses);
  }

  @Transactional(readOnly = true)
  public MatchValuePickResponse getMatchedUserValuePicks() {
    Long userId = authenticationService.getUserId();
    User user = userService.getUserById(userId);
    MatchInfo matchInfo = getMatchInfo(userId);
    User matchedUser = getMatchedUser(userId, matchInfo);
    List<MatchValuePickInnerResponse> matchValuePickInnerResponses = getMatchValuePickInnerResponses(
        user.getProfile().getId(), matchedUser.getProfile().getId());

    return new MatchValuePickResponse(matchInfo.getId(), "",
        matchedUser.getProfile().getProfileBasic().getNickname(), matchValuePickInnerResponses);
  }

  private List<MatchValuePickInnerResponse> getMatchValuePickInnerResponses(Long fromProfileId,
      Long toProfileId) {
    List<ProfileValuePick> profileValuePicksOfFrom =
        profileValuePickService.getAllProfileValuePicksByProfileId(fromProfileId);
    List<ProfileValuePick> profileValuePicksOfTo = profileValuePickService.getAllProfileValuePicksByProfileId(
        toProfileId);

    List<MatchValuePickInnerResponse> talkInnerResponses = new ArrayList<>();
    int valueListSize = profileValuePicksOfFrom.size();
    for (int i = 0; i < valueListSize; i++) {
      ProfileValuePick profileValuePickFrom = profileValuePicksOfFrom.get(i);
      ProfileValuePick profileValuePickTo = profileValuePicksOfTo.get(i);
      String category = profileValuePickTo.getValuePick().getCategory();
      String question = profileValuePickTo.getValuePick().getQuestion();
      Integer selectedAnswer = profileValuePickTo.getSelectedAnswer();
      Map<Integer, Object> answers = profileValuePickTo.getValuePick().getAnswers();
      if (profileValuePickTo.getSelectedAnswer()
          .equals(profileValuePickFrom.getSelectedAnswer())) {
        talkInnerResponses.add(
            new MatchValuePickInnerResponse(category, question, true, answers,
                selectedAnswer));
      } else {
        talkInnerResponses.add(
            new MatchValuePickInnerResponse(category, question, false, answers,
                selectedAnswer)
        );
      }
    }
    return talkInnerResponses;
  }

  @Transactional(readOnly = true)
  public String getMatchedUserImageUrl() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);
    User matchedUser = getMatchedUser(userId, matchInfo);

    return matchedUser.getProfile().getProfileBasic().getImageUrl();
  }

  private List<String> getMatchedValues(Long fromProfileId, Long toProfileId) {
    List<ProfileValuePick> profileValuePicksOfFrom =
        profileValuePickService.getAllProfileValuePicksByProfileId(fromProfileId);
    List<ProfileValuePick> profileValuePicksOfTo = profileValuePickService.getAllProfileValuePicksByProfileId(
        toProfileId);

    int valueListSize = profileValuePicksOfFrom.size();
    List<String> matchedValues = new ArrayList<>();
    for (int i = 0; i < valueListSize; i++) {
      ProfileValuePick profileValuePickOfFrom = profileValuePicksOfFrom.get(i);
      ProfileValuePick profileValuePickOfTo = profileValuePicksOfTo.get(i);
      if (profileValuePickOfFrom.getSelectedAnswer()
          .equals(profileValuePickOfTo.getSelectedAnswer())) {
        Integer selectedAnswer = profileValuePickOfTo.getSelectedAnswer();
        Map<Integer, Object> answers = profileValuePickOfTo.getValuePick().getAnswers();
        String value = (String) answers.get(selectedAnswer);
        matchedValues.add(value);
      }
    }
    return matchedValues;
  }

  @Transactional
  public void acceptMatch() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);
    matchInfo.acceptPiece(userId);
  }

  @Transactional(readOnly = true)
  public Map<ContactType, String> getContacts() {
    Long userId = authenticationService.getUserId();
    MatchInfo matchInfo = getMatchInfo(userId);
    if (!matchInfo.getUser1Accepted() || !matchInfo.getUser2Accepted()) {
      throw new ApplicationException(MatchErrorCode.MATCH_NOT_ACCEPTED);
    }
    User matchedUser = getMatchedUser(userId, matchInfo);
    return matchedUser.getProfile().getProfileBasic().getContacts();
  }

  @Transactional
  public void refuseMatch(Long userId) {
    MatchInfo matchInfo = getMatchInfo(userId);
    matchInfo.refusePiece(userId);
  }
}
