package org.yapp.core.domain.profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileTalkSimilarityHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long profileValueTalkId;

    @Column(columnDefinition = "TEXT")
    private String oldText;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String newText;

    @Column(nullable = false)
    private Double similarity;

    @Column(columnDefinition = "TEXT")
    private String oldSummaryText;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String newSummaryText;

    @Column(nullable = false)
    private Double summarySimilarity;

    @Column(nullable = false)
    private String similarityPipeLineInfo;

    @Column(nullable = false)
    private Boolean aiSummaryTriggered;

    @Builder
    public ProfileTalkSimilarityHistory(Long userId, Long profileValueTalkId, String oldText,
        String newText, Double similarity, String oldSummaryText, String newSummaryText,
        Double summarySimilarity, String similarityPipeLineInfo, Boolean aiSummaryTriggered) {
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