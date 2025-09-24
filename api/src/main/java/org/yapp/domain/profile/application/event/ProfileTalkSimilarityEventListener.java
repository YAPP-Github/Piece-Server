package org.yapp.domain.profile.application.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.profile.ProfileTalkSimilarityHistory;
import org.yapp.core.domain.profile.event.ProfileTalkSimilarityAnalyzedEvent;
import org.yapp.domain.profile.dao.ProfileTalkSimilarityHistoryRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileTalkSimilarityEventListener {

    private final ProfileTalkSimilarityHistoryRepository profileTalkSimilarityHistoryRepository;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleTextSimilarityAnalyzed(ProfileTalkSimilarityAnalyzedEvent event) {
        try {
            ProfileTalkSimilarityHistory history = ProfileTalkSimilarityHistory.builder()
                .userId(event.getUserId())
                .profileValueTalkId(event.getProfileValueTalkId())
                .oldText(event.getOldText())
                .newText(event.getNewText())
                .similarity(event.getSimilarity())
                .oldSummaryText(event.getOldSummaryText())
                .newSummaryText(event.getNewSummaryText())
                .summarySimilarity(event.getSummarySimilarity())
                .similarityPipeLineInfo(event.getSimilarityPipeLineInfo())
                .aiSummaryTriggered(event.isAiSummaryTriggered())
                .build();

            profileTalkSimilarityHistoryRepository.save(history);

            log.debug(
                "Text similarity history saved for user: {}, profileValueTalkId: {}, similarity: {:.3f}",
                event.getUserId(), event.getProfileValueTalkId(), event.getSimilarity());
        } catch (Exception e) {
            log.error("Failed to save text similarity history for user: {}, profileValueTalkId: {}",
                event.getUserId(), event.getProfileValueTalkId(), e);
        }
    }
}