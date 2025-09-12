package org.yapp.core.similarity.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.feature.FeatureVector;

@Slf4j
class JaccardSimilarityCalculatorTest {

    @Test
    @DisplayName("자카드 유사도가 토큰 리스트로 올바르게 계산된다")
    void jaccardCalculator_calculatesCorrectlyWithTokens() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        List<String> tokens1 = List.of("여행", "좋아", "해요");
        List<String> tokens2 = List.of("여행", "싫어", "해요");

        // when
        double similarity = calculator.calculate(tokens1, tokens2);

        // then
        log.info("Tokens 1: {}", tokens1);
        log.info("Tokens 2: {}", tokens2);
        log.info("Jaccard similarity: {}", similarity);

        // 교집합: {여행, 해요} = 2개
        // 합집합: {여행, 좋아, 해요, 싫어} = 4개
        // 자카드 유사도: 2/4 = 0.5
        assertThat(similarity).isEqualTo(0.5);
    }

    @Test
    @DisplayName("자카드 유사도가 동일한 토큰에 대해 1.0을 반환한다")
    void jaccardCalculator_returnsOneForIdenticalTokens() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        List<String> tokens = List.of("여행", "좋아", "해요");

        // when
        double similarity = calculator.calculate(tokens, tokens);

        // then
        log.info("Identical tokens: {}", tokens);
        log.info("Jaccard similarity: {}", similarity);

        assertThat(similarity).isEqualTo(1.0);
    }

    @Test
    @DisplayName("자카드 유사도가 완전히 다른 토큰에 대해 0.0을 반환한다")
    void jaccardCalculator_returnsZeroForCompletelyDifferentTokens() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        List<String> tokens1 = List.of("여행", "좋아");
        List<String> tokens2 = List.of("독서", "싫어");

        // when
        double similarity = calculator.calculate(tokens1, tokens2);

        // then
        log.info("Tokens 1: {}", tokens1);
        log.info("Tokens 2: {}", tokens2);
        log.info("Jaccard similarity: {}", similarity);

        assertThat(similarity).isEqualTo(0.0);
    }

    @Test
    @DisplayName("자카드 유사도가 중복 토큰을 올바르게 처리한다")
    void jaccardCalculator_handlesDuplicateTokensCorrectly() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        List<String> tokens1 = List.of("여행", "여행", "좋아"); // 중복 포함
        List<String> tokens2 = List.of("여행", "좋아", "해요");

        // when
        double similarity = calculator.calculate(tokens1, tokens2);

        // then
        log.info("Tokens 1 (with duplicates): {}", tokens1);
        log.info("Tokens 2: {}", tokens2);
        log.info("Jaccard similarity: {}", similarity);

        // 집합으로 변환되므로 중복 제거: {여행, 좋아} vs {여행, 좋아, 해요}
        // 교집합: {여행, 좋아} = 2개
        // 합집합: {여행, 좋아, 해요} = 3개
        // 자카드 유사도: 2/3 ≈ 0.667
        assertThat(similarity).isCloseTo(2.0 / 3.0, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    @DisplayName("자카드 유사도가 빈 리스트를 올바르게 처리한다")
    void jaccardCalculator_handlesEmptyListsCorrectly() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        List<String> emptyTokens = List.of();
        List<String> tokens = List.of("여행", "좋아");

        // when & then
        assertThat(calculator.calculate(emptyTokens, emptyTokens)).isEqualTo(1.0); // 둘 다 비어있으면 1.0
        assertThat(calculator.calculate(emptyTokens, tokens)).isEqualTo(0.0);     // 하나가 비어있으면 0.0
        assertThat(calculator.calculate(tokens, emptyTokens)).isEqualTo(0.0);     // 하나가 비어있으면 0.0
    }

    @Test
    @DisplayName("자카드 계산기가 FeatureVector 사용을 거부한다")
    void jaccardCalculator_rejectsFeatureVectorUsage() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();
        FeatureVector vector1 = new FeatureVector(Map.of("여행", 0.5), "TEST");
        FeatureVector vector2 = new FeatureVector(Map.of("좋아", 0.3), "TEST");

        // when & then
        assertThatThrownBy(() -> calculator.calculate(vector1, vector2))
            .isInstanceOf(UnsupportedOperationException.class)
            .hasMessageContaining("Jaccard similarity should use token lists directly");
    }

    @Test
    @DisplayName("자카드 계산기가 올바른 타입과 요구사항을 반환한다")
    void jaccardCalculator_returnsCorrectTypeAndRequirements() {
        // given
        JaccardSimilarityCalculator calculator = new JaccardSimilarityCalculator();

        // when & then
        assertThat(calculator.getType()).isEqualTo("JACCARD");
        assertThat(calculator.requiresFeatureVector()).isFalse(); // 특징 벡터 불필요
    }
}