package org.yapp.domain.profile.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.Profile;
import org.yapp.core.domain.profile.ProfileValueTalk;
import org.yapp.core.domain.user.User;
import org.yapp.core.sse.SsePersonalService;
import org.yapp.core.sse.dto.EventType;
import org.yapp.core.sse.dto.SsePayload;
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

    private static final String SYSTEM_PROMPT = """
            사용자가 작성한 자기소개 또는 가치관 관련 글을 요약해, 상대방이 매력을 느낄 수 있도록 자연스럽고 긍정적인 문장으로 정리해 주세요.
            친근하면서도 진솔한 느낌을 살려 주세요.
            너무 일반적인 표현(예: "좋은 사람을 만나고 싶어요")은 피하고, 개성을 드러낼 수 있도록 해 주세요.
            문장은 짧고 명확하게 작성해 주세요.
            최대 50자로 요약해 주세요.
        """;

    private static final String TEXT_CONDITION = " (제약조건: 글자수 최대 20자, 특수문자 제외)";

    @Transactional(readOnly = true)
    public void summaryProfileValueTalksAsync(Long userId) {
        List<Mono<ProfileValueTalkSummaryResponse>> summarizeTasks = getSummaryTasks(userId);

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
    public void summaryProfileValueTalksSync(Profile profile) {
        List<Mono<ProfileValueTalkSummaryResponse>> summarizeTasks = getSummaryTasks(profile);

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

    private List<Mono<ProfileValueTalkSummaryResponse>> getSummaryTasks(Long userId) {
        User user = userService.getUserById(userId);
        Profile profile = user.getProfile();
        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        return profileValueTalks.stream()
            .map(profileValueTalk -> summarizationService.summarize(SYSTEM_PROMPT,
                    profileValueTalk.getAnswer() + TEXT_CONDITION)
                .map(summarizedAnswer -> {
                    summarizedAnswer = summarizedAnswer.replaceAll("[\"']", "");
                    sentSummaryResponse(userId, profileValueTalk.getId(),
                        summarizedAnswer);

                    return new ProfileValueTalkSummaryResponse(profileValueTalk.getId(),
                        summarizedAnswer);
                }))
            .toList();
    }

    private List<Mono<ProfileValueTalkSummaryResponse>> getSummaryTasks(Profile profile) {
        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        return profileValueTalks.stream()
            .map(profileValueTalk -> summarizationService.summarize(SYSTEM_PROMPT,
                    profileValueTalk.getAnswer() + TEXT_CONDITION)
                .map(summarizedAnswer -> {
                    summarizedAnswer = summarizedAnswer.replaceAll("[\"']", "");
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
