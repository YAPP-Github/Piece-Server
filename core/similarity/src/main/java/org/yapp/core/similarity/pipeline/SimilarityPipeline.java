package org.yapp.core.similarity.pipeline;

import java.util.List;
import org.yapp.core.similarity.calculator.SimilarityCalculator;
import org.yapp.core.similarity.feature.FeatureExtractor;
import org.yapp.core.similarity.feature.FeatureVector;
import org.yapp.core.similarity.preprocessing.TextPreprocessor;

/**
 * 문장 유사도 검사 파이프라인 전처리 → 특징 추출 → 유사도 계산의 3단계를 통합 관리
 */
public class SimilarityPipeline {

    private final TextPreprocessor preprocessor;
    private final FeatureExtractor featureExtractor;
    private final SimilarityCalculator similarityCalculator;

    public SimilarityPipeline(
        TextPreprocessor preprocessor,
        FeatureExtractor featureExtractor,
        SimilarityCalculator similarityCalculator) {

        if (preprocessor == null) {
            throw new IllegalArgumentException("TextPreprocessor cannot be null");
        }
        if (similarityCalculator == null) {
            throw new IllegalArgumentException("SimilarityCalculator cannot be null");
        }
        if (similarityCalculator.requiresFeatureVector() && featureExtractor == null) {
            throw new IllegalArgumentException(
                "FeatureExtractor cannot be null when SimilarityCalculator requires feature vectors");
        }

        this.preprocessor = preprocessor;
        this.featureExtractor = featureExtractor;
        this.similarityCalculator = similarityCalculator;
    }

    /**
     * 두 문장의 유사도를 계산합니다.
     *
     * @param text1 첫 번째 문장
     * @param text2 두 번째 문장
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    public double calculateSimilarity(String text1, String text2) {
        if (text1 == null && text2 == null) {
            return 1.0;
        }

        if (text1 == null || text2 == null) {
            return 0.0;
        }

        // 1단계: 전처리
        List<String> tokens1 = preprocessor.preprocess(text1);
        List<String> tokens2 = preprocessor.preprocess(text2);

        // 전처리 후 빈 토큰 리스트 처리
        if (tokens1.isEmpty() && tokens2.isEmpty()) {
            return 1.0;
        }

        if (tokens1.isEmpty() || tokens2.isEmpty()) {
            return 0.0;
        }

        // 계산기가 특징 벡터를 필요로 하는지 확인
        if (similarityCalculator.requiresFeatureVector()) {
            // 2단계: 특징 추출
            FeatureVector[] features = featureExtractor.extractFeatures(tokens1, tokens2);

            // 3단계: 유사도 계산 (특징 벡터 사용)
            return similarityCalculator.calculate(features[0], features[1]);
        } else {
            // 3단계: 유사도 계산 (토큰 리스트 직접 사용)
            return similarityCalculator.calculate(tokens1, tokens2);
        }
    }

    /**
     * 유사도 계산과 함께 상세 정보를 반환합니다.
     *
     * @param text1 첫 번째 문장
     * @param text2 두 번째 문장
     * @return 상세 분석 결과
     */
    public SimilarityResult calculateSimilarityWithDetails(String text1, String text2) {
        long startTime = System.nanoTime();

        List<String> tokens1 = preprocessor.preprocess(text1);
        List<String> tokens2 = preprocessor.preprocess(text2);

        double similarity;
        FeatureVector[] features = null;

        if (tokens1.isEmpty() && tokens2.isEmpty()) {
            similarity = 1.0;
        } else if (tokens1.isEmpty() || tokens2.isEmpty()) {
            similarity = 0.0;
        } else {
            if (similarityCalculator.requiresFeatureVector()) {
                features = featureExtractor.extractFeatures(tokens1, tokens2);
                similarity = similarityCalculator.calculate(features[0], features[1]);
            } else {
                similarity = similarityCalculator.calculate(tokens1, tokens2);
            }
        }

        long endTime = System.nanoTime();
        long processingTimeNanos = endTime - startTime;

        return new SimilarityResult(
            similarity,
            tokens1,
            tokens2,
            features,
            processingTimeNanos,
            getPipelineInfo()
        );
    }

    /**
     * 파이프라인 구성 정보를 반환합니다.
     *
     * @return 파이프라인 정보
     */
    public String getPipelineInfo() {
        String featureInfo = featureExtractor != null ? featureExtractor.getType() : "NONE";
        return String.format("Pipeline[%s → %s → %s]",
            preprocessor.getPreprocessorInfo(),
            featureInfo,
            similarityCalculator.getType());
    }
}
