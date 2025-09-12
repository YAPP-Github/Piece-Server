package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public class ProfileTalkSimilarityAnalyzedEvent {

    private final Long userId;
    private final Long profileValueTalkId;
    private final String oldText;
    private final String newText;
    private final double similarity;
    private final String oldSummaryText;
    private final String newSummaryText;
    private final double summarySimilarity;
    private final String similarityPipeLineInfo;
    private final boolean aiSummaryTriggered;

    public ProfileTalkSimilarityAnalyzedEvent(Long userId, Long profileValueTalkId, String oldText,
        String newText, double similarity, String oldSummaryText, String newSummaryText,
        double summarySimilarity, String similarityPipeLineInfo, boolean aiSummaryTriggered) {
        this.userId = userId;
        this.profileValueTalkId = profileValueTalkId;
        this.oldText = oldText;
        this.newText = newText;
        this.similarity = similarity;
        this.oldSummaryText = oldSummaryText;
        this.newSummaryText = newSummaryText;
        this.summarySimilarity = summarySimilarity;
        this.similarityPipeLineInfo = similarityPipeLineInfo;
        this.aiSummaryTriggered = aiSummaryTriggered;
    }
}