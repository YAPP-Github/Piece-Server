package org.yapp.core.similarity.calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.calculator.CosineSimilarityCalculator;
import org.yapp.core.similarity.feature.FeatureVector;

@Slf4j
class CosineSimilarityCalculatorTest {

    @Test
    @DisplayName("코사인 유사도가 특징 벡터로 올바르게 계산된다")
    void cosineCalculator_calculatesCorrectlyWithFeatureVectors() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        FeatureVector vector1 = new FeatureVector(Map.of(
            "여행", 0.5,
            "좋아", 0.3,
            "해요", 0.2
        ), "TEST");
        FeatureVector vector2 = new FeatureVector(Map.of(
            "여행", 0.4,
            "싫어", 0.6
        ), "TEST");

        // when
        double similarity = calculator.calculate(vector1, vector2);

        // then
        log.info("Vector 1: {}", vector1.getFeatures());
        log.info("Vector 2: {}", vector2.getFeatures());
        log.info("Cosine similarity: {}", similarity);

        assertThat(similarity).isBetween(0.0, 1.0);
        assertThat(similarity).isPositive(); // "여행"이 공통이므로 0보다 커야 함
    }

    @Test
    @DisplayName("코사인 유사도가 동일한 벡터에 대해 1.0을 반환한다")
    void cosineCalculator_returnsOneForIdenticalVectors() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        FeatureVector vector = new FeatureVector(Map.of(
            "여행", 0.5,
            "좋아", 0.3,
            "해요", 0.2
        ), "TEST");

        // when
        double similarity = calculator.calculate(vector, vector);

        // then
        log.info("Identical vector: {}", vector.getFeatures());
        log.info("Cosine similarity: {}", similarity);

        assertThat(similarity).isCloseTo(1.0, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    @DisplayName("코사인 유사도가 직교하는 벡터에 대해 0.0을 반환한다")
    void cosineCalculator_returnsZeroForOrthogonalVectors() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        FeatureVector vector1 = new FeatureVector(Map.of(
            "여행", 1.0,
            "좋아", 0.0
        ), "TEST");
        FeatureVector vector2 = new FeatureVector(Map.of(
            "여행", 0.0,
            "좋아", 1.0
        ), "TEST");

        // when
        double similarity = calculator.calculate(vector1, vector2);

        // then
        log.info("Vector 1: {}", vector1.getFeatures());
        log.info("Vector 2: {}", vector2.getFeatures());
        log.info("Cosine similarity: {}", similarity);

        assertThat(similarity).isCloseTo(0.0, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    @DisplayName("코사인 유사도가 빈 벡터를 올바르게 처리한다")
    void cosineCalculator_handlesEmptyVectorsCorrectly() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        FeatureVector emptyVector = new FeatureVector(Map.of(), "TEST");
        FeatureVector nonEmptyVector = new FeatureVector(Map.of("여행", 0.5), "TEST");

        // when & then
        assertThat(calculator.calculate(emptyVector, emptyVector)).isEqualTo(1.0); // 둘 다 비어있으면 1.0
        assertThat(calculator.calculate(emptyVector, nonEmptyVector)).isEqualTo(
            0.0); // 하나가 비어있으면 0.0
        assertThat(calculator.calculate(nonEmptyVector, emptyVector)).isEqualTo(
            0.0); // 하나가 비어있으면 0.0
    }

    @Test
    @DisplayName("코사인 계산기가 토큰 리스트 사용을 거부한다")
    void cosineCalculator_rejectsTokenListUsage() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        List<String> tokens1 = List.of("여행", "좋아");
        List<String> tokens2 = List.of("여행", "싫어");

        // when & then
        assertThatThrownBy(() -> calculator.calculate(tokens1, tokens2))
            .isInstanceOf(UnsupportedOperationException.class)
            .hasMessageContaining("CosineSimilarity requires feature vectors");
    }

    @Test
    @DisplayName("코사인 계산기가 올바른 타입과 요구사항을 반환한다")
    void cosineCalculator_returnsCorrectTypeAndRequirements() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();

        // when & then
        assertThat(calculator.getType()).isEqualTo("COSINE");
        assertThat(calculator.requiresFeatureVector()).isTrue(); // 특징 벡터 필요
    }

    @Test
    @DisplayName("코사인 유사도가 벡터 크기에 대해 정규화된다")
    void cosineCalculator_isNormalizedForVectorMagnitude() {
        // given
        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        // 동일한 방향, 다른 크기의 벡터들
        FeatureVector smallVector = new FeatureVector(Map.of("여행", 0.1, "좋아", 0.1), "TEST");
        FeatureVector largeVector = new FeatureVector(Map.of("여행", 1.0, "좋아", 1.0), "TEST");

        // when
        double similarity = calculator.calculate(smallVector, largeVector);

        // then
        log.info("Small vector: {}", smallVector.getFeatures());
        log.info("Large vector: {}", largeVector.getFeatures());
        log.info("Cosine similarity: {}", similarity);

        // 동일한 방향이므로 크기에 관계없이 1.0에 가까워야 함
        assertThat(similarity).isCloseTo(1.0, org.assertj.core.data.Offset.offset(0.001));
    }
}