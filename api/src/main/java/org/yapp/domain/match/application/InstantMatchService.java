package org.yapp.domain.match.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.yapp.core.domain.match.MatchInfo;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileBasic;
import org.yapp.core.domain.user.User;
import org.yapp.core.exception.ApplicationException;
import org.yapp.core.exception.error.code.MatchErrorCode;
import org.yapp.domain.match.application.algorithm.block.MatchBlocker;
import org.yapp.domain.match.dao.FreeInstantMatchHistoryRepository;
import org.yapp.domain.match.dao.MatchInfoRepository;
import org.yapp.domain.match.presentation.dto.response.InstantMatchResponse;
import org.yapp.domain.profile.application.ProfileService;
import org.yapp.domain.user.application.UserService;
import org.yapp.infra.qdrant.application.QdrantService;
import org.yapp.infra.redis.application.RedisService;

@Service
@RequiredArgsConstructor
public class InstantMatchService {

    private static final String USER_VECTOR_COLLECTION = "user_vector";
    private static final String INSTANT_MATCH_QUEUE_PREFIX = "instant_match:";
    private static final int TOP_K_SIZE = 100;
    private static final int SIMILAR_USER_SIZE_THRESHOLD = 3;

    private final QdrantService qdrantService;
    private final MatchBlocker blocker;
    private final RedisService redisService;
    private final MatchService matchService;
    private final UserService userService;
    private final ProfileService profileService;
    private final FreeInstantMatchHistoryRepository freeInstantMatchHistoryRepository;
    private final InstantMatchVectorGenerator instantMatchVectorGenerator;
    private final MatchInfoRepository matchInfoRepository;

    private List<MatchInfo> getInstantMatchFromMeListFromRepository(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before24Hours = now.minusHours(24);

        return matchInfoRepository.
            findInstantMatchesFromMeByUserIdAndDateTime(userId, before24Hours, now);
    }

    private List<MatchInfo> getInstantMatchToMeListFromRepository(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before24Hours = now.minusHours(24);

        return matchInfoRepository.
            findInstantMatchesToMeByUserIdAndDateTime(userId, before24Hours, now);
    }

    /**
     * 오늘 무료로 즉시매칭 가능한지 여부 반환
     *
     * @param userId 매칭 수행 유저 ID
     * @return 무료매칭 가능 여부
     */
    public Boolean canFreeInstantMatchToday(Long userId) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusHours(24);
        return !freeInstantMatchHistoryRepository.existsByUserIdAndMatchedTimeBetween(userId,
            startTime,
            endTime);
    }

    /**
     * 새로운 즉시 매칭 얻기
     *
     * @param userId 매칭 수행 유저 ID
     * @return 새로운 즉시 매칭 ID
     */
    public Long getNewInstantMatch(Long userId) {
        if (!canFreeInstantMatchToday(userId)) {
            // TODO: 유료 매칭이라 결제 기능 구현되면 퍼즐 줄이기
        }
        Long instantMatchedUserId = getInstantMatchedUserId(userId);
        MatchInfo matchInfo = matchService.createMatchInfo(userId, instantMatchedUserId, true);
        return matchInfo.getId();
    }


    /**
     * 즉시 매칭 (나에 의한) 리스트 가져오기
     *
     * @param userId 호출하는 유저 ID
     * @return 매칭 리스트
     */
    public List<InstantMatchResponse> getInstantMatchFromMeList(Long userId) {
        List<MatchInfo> instantMatchList = getInstantMatchFromMeListFromRepository(userId);

        return instantMatchList.stream().map(this::transToMatchInfoResponseFromInstantMatchFromMe)
            .toList();
    }

    /**
     * 즉시 매칭 (상대에 의한) 리스트 가져오기
     *
     * @param userId 호출하는 유저 ID
     * @return 매칭 리스트
     */
    public List<InstantMatchResponse> getInstantMatchToMeList(Long userId) {
        List<MatchInfo> instantMatchList = getInstantMatchToMeListFromRepository(userId);
        return instantMatchList.stream().map(this::transToMatchInfoResponseFromInstantMatchToMe)
            .toList();
    }

    private InstantMatchResponse transToMatchInfoResponseFromInstantMatchFromMe(
        MatchInfo matchInfo) {
        User matchingUser = matchInfo.getUser1();
        User matchedUser = matchInfo.getUser2();
        Profile profile = matchedUser.getProfile();
        ProfileBasic profileBasic = profile.getProfileBasic();

        List<String> matchedValues =
            matchService.getMatchedValuesFromProfileIds(
                matchingUser.getProfile().getId(),
                matchedUser.getProfile().getId()
            );
        boolean isBlocked = matchInfo.determineBlocked(matchingUser.getId());

        return InstantMatchResponse.builder()
            .matchId(matchInfo.getId())
            .matchedUserId(matchedUser.getId())
            .matchStatus(matchService.getMatchStatusFromUserStatusAndOtherUserStatus(
                matchInfo.getUser1MatchStatus(),
                matchInfo.getUser2MatchStatus())
            )
            .description(profileBasic.getDescription())
            .nickname(profileBasic.getNickname())
            .birthYear(
                String.valueOf(profileBasic.getBirthdate().getYear()).substring(2))
            .location(profileBasic.getLocation())
            .job(profileBasic.getJob())
            .matchedValueCount(matchedValues.size())
            .matchedValueList(matchedValues)
            .isBlocked(isBlocked)
            .matchedDateTime(matchInfo.getCreatedAt())
            .build();
    }

    private InstantMatchResponse transToMatchInfoResponseFromInstantMatchToMe(MatchInfo matchInfo) {
        User matchingUser = matchInfo.getUser2();
        User matchedUser = matchInfo.getUser1();
        Profile profile = matchedUser.getProfile();
        ProfileBasic profileBasic = profile.getProfileBasic();

        List<String> matchedValues =
            matchService.getMatchedValuesFromProfileIds(
                matchingUser.getProfile().getId(),
                matchedUser.getProfile().getId()
            );
        boolean isBlocked = matchInfo.determineBlocked(matchingUser.getId());

        return InstantMatchResponse.builder()
            .matchId(matchInfo.getId())
            .matchedUserId(matchedUser.getId())
            .matchStatus(matchService.getMatchStatusFromUserStatusAndOtherUserStatus(
                matchInfo.getUser2MatchStatus(),
                matchInfo.getUser1MatchStatus())
            )
            .description(profileBasic.getDescription())
            .nickname(profileBasic.getNickname())
            .birthYear(
                String.valueOf(profileBasic.getBirthdate().getYear()).substring(2))
            .location(profileBasic.getLocation())
            .job(profileBasic.getJob())
            .matchedValueCount(matchedValues.size())
            .matchedValueList(matchedValues)
            .isBlocked(isBlocked)
            .matchedDateTime(matchInfo.getCreatedAt())
            .build();
    }

    /**
     * 새로운 즉시 매칭 상대 찾기
     *
     * @param userId 즉시매칭 수행 유저 ID
     * @return 매칭 상대
     */
    public Long getInstantMatchedUserId(Long userId) {
        String redisKey = INSTANT_MATCH_QUEUE_PREFIX + userId;
        User user = userService.getUserById(userId);

        // Redis 큐에서 유효한 유저 가져오기
        Long matchedUserId = fetchValidMatchedUserIdFromRedis(redisKey, user);

        // 매칭할 유저를 찾았음
        if (matchedUserId != null) {
            return matchedUserId;
        }

        // Qdrant를 통해 유사 유저 탐색 및 Redis 큐에 저장
        List<Double> userVector = qdrantService.getVectorById(USER_VECTOR_COLLECTION, userId);

        List<Long> excludeList = new ArrayList<>(List.of(userId));
        int matchedCount = fillMatchQueue(redisKey, user, userVector, excludeList);

        if (matchedCount == 0) {
            throw new ApplicationException(MatchErrorCode.NOT_ENOUGH_USER_FOR_INSTANT_MATCH);
        }

        return Long.valueOf(redisService.popLeftFromList(redisKey));
    }

    private Long fetchValidMatchedUserIdFromRedis(String redisKey, User user) {
        String matchedUserIdStr;

        while ((matchedUserIdStr = redisService.popLeftFromList(redisKey)) != null) {
            Long matchedUserId = Long.parseLong(matchedUserIdStr);
            User matchedUser = userService.getUserById(matchedUserId);

            if (!blocker.isBlocked(user.getProfile(), matchedUser.getProfile())) {
                return matchedUserId;
            }
        }

        return null;
    }

    /**
     * 매치 큐 채우기 (빠른 매칭 응답을 위함)
     *
     * @param redisKey    매치 큐 키
     * @param user        매칭 수행 유저
     * @param userVector  유저 벡터
     * @param excludeList 매칭에서 제외할 유저ID들
     * @return 큐에 넣은 매칭 유저 개수
     */
    private int fillMatchQueue(String redisKey, User user, List<Double> userVector,
        List<Long> excludeList) {
        int totalMatched = 0;

        while (totalMatched < SIMILAR_USER_SIZE_THRESHOLD) {
            List<Long> similarUsers = qdrantService.searchVectorIdsExcluding(
                USER_VECTOR_COLLECTION, userVector, TOP_K_SIZE, excludeList);

            if (similarUsers.isEmpty()) {
                break;
            }

            excludeList.addAll(similarUsers);

            List<Profile> similarProfileList = profileService.findProfileListByIdList(
                similarUsers);
            for (Profile similarProfile : similarProfileList) {
                if (blocker.isBlocked(user.getProfile(), similarProfile)) {
                    continue;
                }

                redisService.pushRightToList(redisKey,
                    String.valueOf(similarProfile.getUser().getId()));
                totalMatched++;
            }
        }

        return totalMatched;
    }

    /**
     * Qdrant 벡터 모두 갱신
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateAllQdrantVectors() {
        List<Profile> validProfiles = profileService.getValidProfiles();
        for (Profile profile : validProfiles) {
            User user = profile.getUser();
            List<Double> vector = instantMatchVectorGenerator.generate(profile);
            qdrantService.upsertVector(USER_VECTOR_COLLECTION, user.getId(), vector);
        }
    }

    /**
     * Qdrant 벡터 컬렉션 생성
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void createVectorCollection(int vectorSize) {
        qdrantService.createCollection(USER_VECTOR_COLLECTION, vectorSize, "Manhattan");
    }

    /**
     * Qdrant 벡터 컬렉션 삭제
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteVectorCollection() {
        qdrantService.deleteCollection(USER_VECTOR_COLLECTION);
    }
}
