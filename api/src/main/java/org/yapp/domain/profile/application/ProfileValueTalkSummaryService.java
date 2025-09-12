package org.yapp.domain.profile.application;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.profile.event.ProfileTalkSimilarityAnalyzedEvent;
import org.yapp.core.domain.user.User;
import org.yapp.core.similarity.DocumentSimilarityUtils;
import org.yapp.core.sse.SsePersonalService;
import org.yapp.core.sse.dto.EventType;
import org.yapp.core.sse.dto.SsePayload;
import org.yapp.domain.profile.application.dto.ProfileValueTalkAnswerDto;
import org.yapp.domain.user.application.UserService;
import org.yapp.infra.ai.application.SummarizationService;
import org.yapp.sse.presentation.response.ProfileValueTalkSummaryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileValueTalkSummaryService {

    private final SummarizationService summarizationService;
    private final SsePersonalService ssePersonalService;
    private final ProfileValueTalkService profileValueTalkService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    private static final double SIMILARITY_THRESHOLD = 0.3;

    private static final String SYSTEM_PROMPT = """
            당신은 사용자가 작성한 자기소개/가치관 관련 글을 바탕으로, 소개팅 앱 프로필에 어울리는 문장을 작성하는 역할입니다.
        
            목적
            - 상대방이 매력을 느낄 수 있도록 개성 있고 진솔하게 요약해 주세요.
            - 소개팅 앱 프로필에 적합한 문장으로 구성합니다.
        
            작성 규칙
            - 최대 50자 이내, 가능하면 40자 내외
            - 문장 수: 1문장
            - 따뜻하고 친근한 어조, 진솔한 느낌
            - 사용자의 가치관과 성격이 잘 드러나도록 작성
            - 너무 일반적인 표현은 금지 (예: “좋은 사람을 만나고 싶어요”)
            - 줄바꿈 없이 한 줄로 작성
            - 이모지, 말줄임표(…) 사용 금지
            - 설명 없이 결과 문장만 출력
        
            [입력 예시 1]
            여행과 사진 찍는 걸 좋아하고, 사람들과의 소통을 중요하게 생각해요. 사소한 일상 속에서 행복을 찾는 편이에요. 상대방을 배려하며, 서로 존중하는 관계를 꿈꿔요.
        
            [출력 예시 1]
            소소한 행복을 나누는 따뜻한 사람입니다
        
            [입력 예시 2]
            책 읽는 걸 좋아하고 사색을 즐겨요. 감정 표현에 솔직하고 진심 어린 대화를 좋아하는 편이에요. 깊이 있는 관계를 지향합니다.
        
            [출력 예시 2]
            진심을 나누는 대화를 소중히 여겨요
        
            [입력 예시 3]
            밝고 긍정적인 에너지를 가진 사람입니다. 일할 때는 성실하게, 쉴 때는 여유롭게 보내며 균형을 중요하게 생각해요.
        
            [출력 예시 3]
            긍정과 여유를 담아 하루를 살아가요
        """;

    private static final String TEXT_CONDITION = " (제약조건: 글자수 최대 50자, 특수문자 금지)";

    /**
     * 사용자의 기존 프로필 가치관 답변과 새로운 답변들을 비교해, 변경된 항목에 대해서만 요약을 수행합니다.
     *
     * @param userId                요약을 수행할 대상 사용자의 ID
     * @param newProfileTalkAnswers 사용자가 새로 입력한 프로필 가치관 답변 리스트
     */
    @Transactional(readOnly = true)
    public void summaryProfileValueTalksAsync(Long userId,
        List<ProfileValueTalkAnswerDto> newProfileTalkAnswers) {
        List<Mono<ProfileValueTalkSummaryResponse>> summarizeTasks = getSummaryTasks(userId,
            newProfileTalkAnswers);

        Flux.merge(summarizeTasks)
            .collectList()
            .publishOn(Schedulers.boundedElastic()) // 비동기 처리
            .doOnSuccess(profileValueTalkSummaryResponses -> {
                profileValueTalkSummaryResponses.forEach(response -> {
                    profileValueTalkService.updateSummary(response.profileValueTalkId(),
                        response.summary());
                });
            })
            .subscribe();
    }

    @Transactional
    public void summaryProfileValueTalksSync(Profile currentProfile) {
        List<Mono<ProfileValueTalkSummaryResponse>> summarizeTasks = getSummaryTasks(
            currentProfile);

        List<ProfileValueTalkSummaryResponse> results = Flux.merge(summarizeTasks)
            .collectList()
            .block();

        if (results != null) {
            results.forEach(response -> {
                profileValueTalkService.updateSummary(response.profileValueTalkId(),
                    response.summary());
            });
        }
    }

    private List<Mono<ProfileValueTalkSummaryResponse>> getSummaryTasks(Long userId,
        List<ProfileValueTalkAnswerDto> newProfileTalkAnswers) {
        User user = userService.getUserById(userId);
        Profile profile = user.getProfile();
        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        Map<Long, ProfileValueTalkAnswerDto> newProfileTalkAnswersMap = newProfileTalkAnswers.stream()
            .collect(Collectors.toMap(
                ProfileValueTalkAnswerDto::profileValueTalkId,
                Function.identity()
            ));

        return profileValueTalks.stream()
            .map(profileValueTalk -> {

                String currentAnswer = profileValueTalk.getAnswer();

                String newAnswer = Optional.ofNullable(
                        newProfileTalkAnswersMap.get(profileValueTalk.getId()))
                    .map(ProfileValueTalkAnswerDto::answer)
                    .orElse(null);

                // 텍스트 변경이 있는 경우 유사도 계산
                if (newAnswer != null && !newAnswer.equals(currentAnswer)) {
                    double similarity = DocumentSimilarityUtils.calculateSimilarity(
                        currentAnswer, newAnswer);

                    String similarityPipelineInfo = DocumentSimilarityUtils.getDefaultPipelineInfo();
                    boolean isSignificantChange = similarity < SIMILARITY_THRESHOLD;

                    // 유의미한 변경이 있는 경우에만 요약 실행
                    if (isSignificantChange) {
                        return summarizationService.summarize(SYSTEM_PROMPT,
                                newAnswer + TEXT_CONDITION)
                            .map(summarizedAnswer -> {
                                summarizedAnswer = summarizedAnswer.replaceAll("[\"']", "");
                                summarizedAnswer = summarizedAnswer.substring(0,
                                    Math.min(summarizedAnswer.length(), 50));

                                // 요약 완료 후 유사도 계산 및 이벤트 발행
                                String currentSummary = profileValueTalk.getSummary();
                                double summarySimilarity = DocumentSimilarityUtils.calculateSimilarity(
                                    currentSummary, summarizedAnswer);

                                eventPublisher.publishEvent(new ProfileTalkSimilarityAnalyzedEvent(
                                    userId, profileValueTalk.getId(), currentAnswer, newAnswer,
                                    similarity, currentSummary, summarizedAnswer, summarySimilarity,
                                    similarityPipelineInfo, true
                                ));

                                sentSummaryResponse(userId, profileValueTalk.getId(),
                                    summarizedAnswer);
                                return new ProfileValueTalkSummaryResponse(profileValueTalk.getId(),
                                    summarizedAnswer);
                            });
                    } else {
                        eventPublisher.publishEvent(new ProfileTalkSimilarityAnalyzedEvent(
                            userId, profileValueTalk.getId(), currentAnswer, newAnswer,
                            similarity, profileValueTalk.getSummary(),
                            profileValueTalk.getSummary(),
                            1.0, similarityPipelineInfo, false
                        ));
                    }
                }

                // 변경이 없거나 유의미하지 않은 변경인 경우 기존 summary 사용
                sentSummaryResponse(userId, profileValueTalk.getId(),
                    profileValueTalk.getSummary());
                return Mono.just(new ProfileValueTalkSummaryResponse(
                    profileValueTalk.getId(),
                    profileValueTalk.getSummary() // 기존 요약값 사용
                ));
            })
            .toList();
    }

    private List<Mono<ProfileValueTalkSummaryResponse>> getSummaryTasks(Profile profile) {
        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        return profileValueTalks.stream()
            .map(profileValueTalk -> summarizationService.summarize(SYSTEM_PROMPT,
                    profileValueTalk.getAnswer() + TEXT_CONDITION)
                .map(summarizedAnswer -> {
                    summarizedAnswer = summarizedAnswer.replaceAll("[\"']", "");
                    summarizedAnswer = summarizedAnswer.substring(0,
                        Math.min(summarizedAnswer.length(), 50));

                    return new ProfileValueTalkSummaryResponse(profileValueTalk.getId(),
                        summarizedAnswer);
                }))
            .toList();
    }

    private void sentSummaryResponse(Long userId, Long profileValueTalkId, String summary) {
        ProfileValueTalkSummaryResponse response = new ProfileValueTalkSummaryResponse(
            profileValueTalkId, summary);

        ssePersonalService.sendToPersonal(userId, SsePayload.of(
            null, EventType.PROFILE_TALK_SUMMARY_MESSAGE, response
        ));
    }
}
