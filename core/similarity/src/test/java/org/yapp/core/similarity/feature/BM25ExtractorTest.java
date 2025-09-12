package org.yapp.core.similarity.feature;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class BM25ExtractorTest {

    @Test
    @DisplayName("BM25 추출기가 기본적인 특징 벡터를 생성한다")
    void bm25Extractor_createsBasicFeatureVectors() {
        // given
        BM25Extractor extractor = new BM25Extractor();
        List<String> tokens1 = List.of("여행", "좋아", "여행");
        List<String> tokens2 = List.of("여행", "싫어");

        // when
        FeatureVector[] vectors = extractor.extractFeatures(tokens1, tokens2);

        // then
        log.info("Vector 1 features: {}", vectors[0].getFeatures());
        log.info("Vector 2 features: {}", vectors[1].getFeatures());
        log.info("Extractor type: {}", extractor.getType());

        assertThat(vectors).hasSize(2);
        assertThat(vectors[0].getExtractorType()).isEqualTo("BM25");
        assertThat(vectors[1].getExtractorType()).isEqualTo("BM25");

        // 공통 토큰 "여행"이 두 벡터에 모두 존재해야 함
        assertThat(vectors[0].getFeatures()).containsKey("여행");
        assertThat(vectors[1].getFeatures()).containsKey("여행");
    }

    @Test
    @DisplayName("BM25가 문서 길이 정규화를 적용한다")
    void bm25Extractor_appliesDocumentLengthNormalization() {
        // given
        BM25Extractor extractor = new BM25Extractor();
        List<String> shortDoc = List.of("여행"); // 짧은 문서
        List<String> longDoc = List.of("여행", "정말", "좋아", "해요", "진짜", "좋아"); // 긴 문서

        // when
        FeatureVector[] vectors = extractor.extractFeatures(shortDoc, longDoc);

        // then
        Map<String, Double> shortFeatures = vectors[0].getFeatures();
        Map<String, Double> longFeatures = vectors[1].getFeatures();

        log.info("Short doc '여행' score: {}", shortFeatures.get("여행"));
        log.info("Long doc '여행' score: {}", longFeatures.get("여행"));

        // BM25는 문서 길이를 정규화하므로 점수가 다를 것
        assertThat(shortFeatures.get("여행")).isPositive();
        assertThat(longFeatures.get("여행")).isPositive();
        assertThat(shortFeatures.get("여행")).isNotEqualTo(longFeatures.get("여행"));
    }

    @Test
    @DisplayName("BM25가 항목 빈도 포화를 적용한다")
    void bm25Extractor_appliesTermFrequencySaturation() {
        // given
        BM25Extractor extractor = new BM25Extractor();
        List<String> normalDoc = List.of("여행", "좋아");
        List<String> repetitiveDoc = List.of("여행", "여행", "여행", "여행", "좋아");

        // when
        FeatureVector[] vectors = extractor.extractFeatures(normalDoc, repetitiveDoc);

        // then
        Map<String, Double> normalFeatures = vectors[0].getFeatures();
        Map<String, Double> repetitiveFeatures = vectors[1].getFeatures();

        log.info("Normal doc '여행' score: {}", normalFeatures.get("여행"));
        log.info("Repetitive doc '여행' score: {}", repetitiveFeatures.get("여행"));

        // BM25는 항목 빈도 포화로 인해 무한정 증가하지 않음
        assertThat(normalFeatures.get("여행")).isPositive();
        assertThat(repetitiveFeatures.get("여행")).isPositive();

        assertThat(repetitiveFeatures.get("여행")).isGreaterThan(normalFeatures.get("여행"));
    }

    @Test
    @DisplayName("BM25가 빈 토큰 리스트를 처리한다")
    void bm25Extractor_handlesEmptyTokens() {
        // given
        BM25Extractor extractor = new BM25Extractor();
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
    @DisplayName("BM25 추출기가 올바른 타입을 반환한다")
    void bm25Extractor_returnsCorrectType() {
        // given
        BM25Extractor extractor = new BM25Extractor();

        // when & then
        assertThat(extractor.getType()).isEqualTo("BM25");
    }
}