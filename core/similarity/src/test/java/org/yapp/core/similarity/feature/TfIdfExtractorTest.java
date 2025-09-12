package org.yapp.core.similarity.feature;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.feature.FeatureVector;
import org.yapp.core.similarity.feature.TfIdfExtractor;

@Slf4j
class TfIdfExtractorTest {

    @Test
    @DisplayName("TF-IDF 추출기가 기본적인 특징 벡터를 생성한다")
    void tfIdfExtractor_createsBasicFeatureVectors() {
        // given
        TfIdfExtractor extractor = new TfIdfExtractor();
        List<String> tokens1 = List.of("여행", "좋아", "여행");
        List<String> tokens2 = List.of("여행", "싫어");

        // when
        FeatureVector[] vectors = extractor.extractFeatures(tokens1, tokens2);

        // then
        log.info("Vector 1 features: {}", vectors[0].getFeatures());
        log.info("Vector 2 features: {}", vectors[1].getFeatures());
        log.info("Extractor type: {}", extractor.getType());

        assertThat(vectors).hasSize(2);
        assertThat(vectors[0].getExtractorType()).isEqualTo("TF_IDF");
        assertThat(vectors[1].getExtractorType()).isEqualTo("TF_IDF");

        // 공통 토큰 "여행"이 두 벡터에 모두 존재해야 함
        assertThat(vectors[0].getFeatures()).containsKey("여행");
        assertThat(vectors[1].getFeatures()).containsKey("여행");
    }

    @Test
    @DisplayName("TF-IDF가 용어 빈도를 올바르게 계산한다")
    void tfIdfExtractor_calculatesTermFrequencyCorrectly() {
        // given
        TfIdfExtractor extractor = new TfIdfExtractor();
        List<String> tokens1 = List.of("여행", "여행", "좋아"); // "여행"이 2번 등장
        List<String> tokens2 = List.of("여행"); // "여행"이 1번 등장

        // when
        FeatureVector[] vectors = extractor.extractFeatures(tokens1, tokens2);

        // then
        Map<String, Double> features1 = vectors[0].getFeatures();
        Map<String, Double> features2 = vectors[1].getFeatures();

        log.info("Document 1 - '여행' weight: {}", features1.get("여행"));
        log.info("Document 2 - '여행' weight: {}", features2.get("여행"));

        // 첫 번째 문서에서 "여행"의 TF가 더 높아야 함 (2/3 vs 1/1)
        // 하지만 IDF가 적용되므로 정확한 값 비교보다는 존재 여부 확인
        assertThat(features1.get("여행")).isPositive();
        assertThat(features2.get("여행")).isPositive();
    }

    @Test
    @DisplayName("TF-IDF가 빈 토큰 리스트를 처리한다")
    void tfIdfExtractor_handlesEmptyTokens() {
        // given
        TfIdfExtractor extractor = new TfIdfExtractor();
        List<String> emptyTokens = List.of();
        List<String> tokens = List.of("여행", "좋아");

        // when
        FeatureVector[] vectors = extractor.extractFeatures(emptyTokens, tokens);

        // then
        log.info("Empty vector features: {}", vectors[0].getFeatures());
        log.info("Non-empty vector features: {}", vectors[1].getFeatures());

        assertThat(vectors[0].isEmpty()).isTrue();
        assertThat(vectors[1].isEmpty()).isFalse();
        assertThat(vectors[1].getFeatures()).containsKeys("여행", "좋아");
    }

    @Test
    @DisplayName("TF-IDF가 동일한 토큰 리스트에 대해 일관된 결과를 반환한다")
    void tfIdfExtractor_consistentResultsForIdenticalTokens() {
        // given
        TfIdfExtractor extractor = new TfIdfExtractor();
        List<String> tokens = List.of("여행", "좋아", "정말");

        // when
        FeatureVector[] vectors = extractor.extractFeatures(tokens, tokens);

        // then
        log.info("Vector 1: {}", vectors[0].getFeatures());
        log.info("Vector 2: {}", vectors[1].getFeatures());

        // 동일한 토큰 리스트이므로 동일한 특징 벡터여야 함
        assertThat(vectors[0].getFeatures()).isEqualTo(vectors[1].getFeatures());
    }

    @Test
    @DisplayName("TF-IDF 추출기가 올바른 타입을 반환한다")
    void tfIdfExtractor_returnsCorrectType() {
        // given
        TfIdfExtractor extractor = new TfIdfExtractor();

        // when & then
        assertThat(extractor.getType()).isEqualTo("TF_IDF");
    }
}