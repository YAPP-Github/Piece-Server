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
        
            예시 입력:
            "여행을 정말 좋아해요! 특히 혼자 떠나는 여행에서 새로운 사람들과 이야기를 나누는 걸 좋아합니다. 주말에는 가까운 곳이라도 가볍게 나들이 가는 걸 즐기고, 때로는 집에서 독서를 하면서 조용한 시간을 보내기도 해요. 감성적인 영화 보는 것도 좋아해서, 좋은 영화 추천해 주시면 너무 반가울 것 같아요!"
        
            예시 출력:
            "새로운 경험과 사람을 좋아하는 여행 마니아! 가벼운 나들이부터 감성적인 영화 감상까지, 일상의 작은 순간들을 소중히 여깁니다."
        """;

    private static final String TEXT_CONDITION = " (제약조건: 글자수 최대 20자, 특수문자 제외)";

    @Transactional(readOnly = true)
    public void summaryProfileValueTalks(Long userId) {
        User user = userService.getUserById(userId);
        Profile profile = user.getProfile();
        List<ProfileValueTalk> profileValueTalks = profile.getProfileValueTalks();

        List<Mono<ProfileValueTalkSummaryResponse>> summarizeTasks = profileValueTalks.stream()
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

        Flux.merge(summarizeTasks)
            .collectList()
            .publishOn(Schedulers.boundedElastic())
            .doOnSuccess(profileValueTalkSummaryResponses -> {
                profileValueTalkSummaryResponses.forEach(profileValueTalkSummaryResponse -> {
                    profileValueTalkService.updateSummary(
                        profileValueTalkSummaryResponse.profileValueTalkId(),
                        profileValueTalkSummaryResponse.summary());
                });
            })
            .subscribe();
    }

    private void sentSummaryResponse(Long userId, Long profileValueTalkId, String summary) {
        ProfileValueTalkSummaryResponse response = new ProfileValueTalkSummaryResponse(
            profileValueTalkId,
            summary
        );

        ssePersonalService.sendToPersonal(userId, SsePayload.of(
            null, EventType.PROFILE_TALK_SUMMARY_MESSAGE, response
        ));
    }
}
