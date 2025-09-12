package org.yapp.core.similarity.feature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.calculator.CosineSimilarityCalculator;
import org.yapp.core.similarity.calculator.JaccardSimilarityCalculator;
import org.yapp.core.similarity.pipeline.SimilarityPipeline;
import org.yapp.core.similarity.pipeline.SimilarityResult;
import org.yapp.core.similarity.preprocessing.TextPreprocessor;
import org.yapp.core.similarity.preprocessing.cleaner.KoreanTextCleaner;
import org.yapp.core.similarity.preprocessing.remover.KoreanStopWordRemover;
import org.yapp.core.similarity.preprocessing.tokenizer.KomoranTokenizer;

@Slf4j
class SimilarityComparisonTest {

    @Test
    @DisplayName("Komoran + TF + Cosine vs TF-IDF + Cosine vs Jaccard 비교 테스트")
    void compareThreeSimilarityMethods() {
        // given
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        // 1. TF + Cosine 파이프라인
        SimilarityPipeline tfCosinePipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new TfExtractor(),
            new CosineSimilarityCalculator()
        );

        // 2. TF-IDF + Cosine 파이프라인  
        SimilarityPipeline tfidfCosinePipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );

        // 3. Jaccard 파이프라인
        SimilarityPipeline jaccardPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            null,
            new JaccardSimilarityCalculator()
        );

        // 테스트 케이스 1: 유사한 문장
        String similarText1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String similarText2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        SimilarityResult tfCosineResult1 = tfCosinePipeline.calculateSimilarityWithDetails(
            similarText1, similarText2);
        SimilarityResult tfidfCosineResult1 = tfidfCosinePipeline.calculateSimilarityWithDetails(
            similarText1, similarText2);
        SimilarityResult jaccardResult1 = jaccardPipeline.calculateSimilarityWithDetails(
            similarText1, similarText2);

        // then
        log.info("=== 유사한 문장 테스트 ===");
        log.info("Text 1: [{}]", similarText1);
        log.info("Text 2: [{}]", similarText2);
        log.info("TF + Cosine: {}", String.format("%.4f", tfCosineResult1.getSimilarity()));
        log.info("TF-IDF + Cosine: {}", String.format("%.4f", tfidfCosineResult1.getSimilarity()));
        log.info("Jaccard: {}", String.format("%.4f", jaccardResult1.getSimilarity()));
        log.info("TF 토큰 1: {}", tfCosineResult1.getProcessedTokens1());
        log.info("TF 토큰 2: {}", tfCosineResult1.getProcessedTokens2());

        assertThat(tfCosineResult1.getSimilarity()).isBetween(0.0, 1.0);
        assertThat(tfidfCosineResult1.getSimilarity()).isBetween(0.0, 1.0);
        assertThat(jaccardResult1.getSimilarity()).isBetween(0.0, 1.0);

        // 테스트 케이스 2: 다른 주제의 문장
        String differentText1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String differentText2 = "최근에는 효율적인 데이터 처리를 위한 백엔드 시스템 설계에 집중하고 있습니다";

        // when
        SimilarityResult tfCosineResult2 = tfCosinePipeline.calculateSimilarityWithDetails(
            differentText1, differentText2);
        SimilarityResult tfidfCosineResult2 = tfidfCosinePipeline.calculateSimilarityWithDetails(
            differentText1, differentText2);
        SimilarityResult jaccardResult2 = jaccardPipeline.calculateSimilarityWithDetails(
            differentText1, differentText2);

        // then
        log.info("\n=== 다른 주제 문장 테스트 ===");
        log.info("Text 1: [{}]", differentText1);
        log.info("Text 2: [{}]", differentText2);
        log.info("TF + Cosine: {}", String.format("%.4f", tfCosineResult2.getSimilarity()));
        log.info("TF-IDF + Cosine: {}", String.format("%.4f", tfidfCosineResult2.getSimilarity()));
        log.info("Jaccard: {}", String.format("%.4f", jaccardResult2.getSimilarity()));

        // 테스트 케이스 3: 완전히 동일한 문장
        String identicalText = "저는 여행을 좋아합니다";

        // when
        SimilarityResult tfCosineResult3 = tfCosinePipeline.calculateSimilarityWithDetails(
            identicalText, identicalText);
        SimilarityResult tfidfCosineResult3 = tfidfCosinePipeline.calculateSimilarityWithDetails(
            identicalText, identicalText);
        SimilarityResult jaccardResult3 = jaccardPipeline.calculateSimilarityWithDetails(
            identicalText, identicalText);

        // then
        log.info("\n=== 동일 문장 테스트 ===");
        log.info("Text: [{}]", identicalText);
        log.info("TF + Cosine: {}", String.format("%.4f", tfCosineResult3.getSimilarity()));
        log.info("TF-IDF + Cosine: {}", String.format("%.4f", tfidfCosineResult3.getSimilarity()));
        log.info("Jaccard: {}", String.format("%.4f", jaccardResult3.getSimilarity()));

        // 동일 문장은 모든 방법에서 1.0이어야 함
        assertThat(tfCosineResult3.getSimilarity()).isCloseTo(1.0, within(0.00001));
        assertThat(tfidfCosineResult3.getSimilarity()).isCloseTo(1.0, within(0.00001));
        assertThat(jaccardResult3.getSimilarity()).isCloseTo(1.0, within(0.00001));

        // 비교 분석
        log.info("\n=== 결과 분석 ===");
        log.info("유사한 문장에서 TF+Cosine이 TF-IDF+Cosine보다 높은가? {}",
            tfCosineResult1.getSimilarity() > tfidfCosineResult1.getSimilarity());
        log.info("다른 주제에서 TF+Cosine이 TF-IDF+Cosine보다 높은가? {}",
            tfCosineResult2.getSimilarity() > tfidfCosineResult2.getSimilarity());
    }

    @Test
    @DisplayName("긴 문장에서의 세 가지 방법 비교")
    void compareLongTexts() {
        // given
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline tfCosinePipeline = new SimilarityPipeline(
            komoranPreprocessor, new TfExtractor(), new CosineSimilarityCalculator());
        SimilarityPipeline tfidfCosinePipeline = new SimilarityPipeline(
            komoranPreprocessor, new TfIdfExtractor(), new CosineSimilarityCalculator());
        SimilarityPipeline jaccardPipeline = new SimilarityPipeline(
            komoranPreprocessor, null, new JaccardSimilarityCalculator());

        // 100자 내외의 긴 문장
        String longText1 = "새로운 장소를 탐험하고 여러 문화를 경험하는 여행은 제게 큰 영감을 줍니다. 낯선 거리에서 느끼는 설렘과 현지 음식을 맛보는 즐거움은 삶의 활력소가 됩니다.";
        String longText2 = "저는 여행이 주는 특별한 경험을 사랑합니다. 새로운 사람들을 만나고 그들의 이야기를 듣는 것은 세상을 보는 시야를 넓혀주죠. 그래서 항상 다음 목적지를 꿈꿉니다.";

        // when
        double tfCosineSimilarity = tfCosinePipeline.calculateSimilarity(longText1, longText2);
        double tfidfCosineSimilarity = tfidfCosinePipeline.calculateSimilarity(longText1,
            longText2);
        double jaccardSimilarity = jaccardPipeline.calculateSimilarity(longText1, longText2);

        // then
        log.info("=== 긴 문장 비교 테스트 ===");
        log.info("Text 1: [{}]", longText1);
        log.info("Text 2: [{}]", longText2);
        log.info("TF + Cosine: {}", String.format("%.4f", tfCosineSimilarity));
        log.info("TF-IDF + Cosine: {}", String.format("%.4f", tfidfCosineSimilarity));
        log.info("Jaccard: {}", String.format("%.4f", jaccardSimilarity));

        assertThat(tfCosineSimilarity).isBetween(0.0, 1.0);
        assertThat(tfidfCosineSimilarity).isBetween(0.0, 1.0);
        assertThat(jaccardSimilarity).isBetween(0.0, 1.0);
    }
}