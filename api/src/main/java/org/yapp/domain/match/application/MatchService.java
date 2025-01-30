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
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileValuePick;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.domain.match.dao.MatchInfoRepository;
import org.yapp.domain.match.presentation.dto.response.MatchInfoResponse;
import org.yapp.domain.match.presentation.dto.response.MatchProfileBasicResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValuePickInnerResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValuePickResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValueTalkInnerResponse;
import org.yapp.domain.match.presentation.dto.response.MatchValueTalkResponse;
import org.yapp.domain.profile.application.ProfileValuePickService;
import org.yapp.domain.user.application.UserService;
import org.yapp.error.dto.MatchErrorCode;
import org.yapp.error.exception.ApplicationException;

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

        //TODO : 왜 deprecated 된 ProfileBio에만 introduce가 있는지 논의가 필요
        MatchInfoResponse response = MatchInfoResponse.builder()
            .matchId(matchInfo.getId())
            .matchStatus(getMatchStatus(userId, matchInfo))
            .shortIntroduce("") // Deprecated 된 BIO 에서 넣어야하는지?
            .nickname(matchedUser.getProfile().getProfileBasic().getNickname())
            .birthYear(
                String.valueOf(matchedUser.getProfile().getProfileBasic().getBirthdate().getYear()))
            .location(matchedUser.getProfile().getProfileBasic().getLocation())
            .job(matchedUser.getProfile().getProfileBasic().getJob())
            .matchedValueCount(matchedValues.size())
            .matchedValueList(matchedValues)
            .build();

        return response;
    }

    private User getMatchedUser(Long userId, MatchInfo matchInfo) {
        if (userId.equals(matchInfo.getUser1().getId())) {
            return matchInfo.getUser2();
        }
        return matchInfo.getUser1();
    }

    private String getMatchStatus(Long userId, MatchInfo matchInfo) {
        if (userId.equals(matchInfo.getUser1().getId())) {
            if (!matchInfo.getUser1PieceChecked()) {
                return MatchStatus.BEFORE_OPEN.getStatus();
            }
            if (matchInfo.getUser1Accepted() && matchInfo.getUser2Accepted()) {
                return MatchStatus.MATCHED.getStatus();
            }
            if (matchInfo.getUser1Accepted()) {
                return MatchStatus.RESPONDED.getStatus();
            }
            if (matchInfo.getUser2Accepted()) {
                return MatchStatus.GREEN_LIGHT.getStatus();
            }
            return MatchStatus.WAITING.getStatus();
        } else {
            if (!matchInfo.getUser2PieceChecked()) {
                return MatchStatus.BEFORE_OPEN.getStatus();
            }
            if (matchInfo.getUser1Accepted() && matchInfo.getUser2Accepted()) {
                return MatchStatus.MATCHED.getStatus();
            }
            if (matchInfo.getUser2Accepted()) {
                return MatchStatus.RESPONDED.getStatus();
            }
            if (matchInfo.getUser1Accepted()) {
                return MatchStatus.GREEN_LIGHT.getStatus();
            }
            return MatchStatus.WAITING.getStatus();
        }
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
    public Map<String, String> getContacts() {
        Long userId = authenticationService.getUserId();
        MatchInfo matchInfo = getMatchInfo(userId);
        if (!matchInfo.getUser1Accepted() || !matchInfo.getUser2Accepted()) {
            throw new ApplicationException(MatchErrorCode.MATCH_NOT_ACCEPTED);
        }
        User matchedUser = getMatchedUser(userId, matchInfo);
        return matchedUser.getProfile().getProfileBasic().getContacts();
    }
}
