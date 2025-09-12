package org.yapp.core.similarity.pipeline;

import org.yapp.core.similarity.feature.FeatureVector;

import java.util.List;

/**
 * 유사도 계산 결과를 담는 클래스
 * 유사도 값과 함께 처리 과정의 상세 정보를 포함
 */
public class SimilarityResult {
    
    private final double similarity;
    private final List<String> processedTokens1;
    private final List<String> processedTokens2;
    private final FeatureVector[] featureVectors;
    private final long processingTimeNanos;
    private final String pipelineInfo;
    
    public SimilarityResult(double similarity,
                          List<String> processedTokens1,
                          List<String> processedTokens2,
                          FeatureVector[] featureVectors,
                          long processingTimeNanos,
                          String pipelineInfo) {
        this.similarity = similarity;
        this.processedTokens1 = List.copyOf(processedTokens1);
        this.processedTokens2 = List.copyOf(processedTokens2);
        this.featureVectors = featureVectors;
        this.processingTimeNanos = processingTimeNanos;
        this.pipelineInfo = pipelineInfo;
    }
    
    /**
     * 계산된 유사도 값을 반환합니다.
     * 
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    public double getSimilarity() {
        return similarity;
    }
    
    /**
     * 첫 번째 텍스트의 전처리된 토큰들을 반환합니다.
     * 
     * @return 전처리된 토큰 리스트
     */
    public List<String> getProcessedTokens1() {
        return processedTokens1;
    }
    
    /**
     * 두 번째 텍스트의 전처리된 토큰들을 반환합니다.
     * 
     * @return 전처리된 토큰 리스트
     */
    public List<String> getProcessedTokens2() {
        return processedTokens2;
    }
    
    /**
     * 특징 벡터들을 반환합니다.
     * 
     * @return 특징 벡터 배열 (특징 추출을 사용하지 않은 경우 null)
     */
    public FeatureVector[] getFeatureVectors() {
        return featureVectors;
    }
    
    /**
     * 처리 시간을 나노초 단위로 반환합니다.
     * 
     * @return 처리 시간 (나노초)
     */
    public long getProcessingTimeNanos() {
        return processingTimeNanos;
    }
    
    /**
     * 처리 시간을 밀리초 단위로 반환합니다.
     * 
     * @return 처리 시간 (밀리초)
     */
    public double getProcessingTimeMillis() {
        return processingTimeNanos / 1_000_000.0;
    }
    
    /**
     * 파이프라인 정보를 반환합니다.
     * 
     * @return 파이프라인 구성 정보
     */
    public String getPipelineInfo() {
        return pipelineInfo;
    }
    
    /**
     * 상세 분석 결과를 문자열로 반환합니다.
     * 
     * @return 상세 분석 결과
     */
    public String getDetailedAnalysis() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Similarity Analysis Result ===\n");
        sb.append(String.format("Pipeline: %s\n", pipelineInfo));
        sb.append(String.format("Similarity: %.4f\n", similarity));
        sb.append(String.format("Processing Time: %.2f ms\n", getProcessingTimeMillis()));
        sb.append("\n--- Preprocessing Results ---\n");
        sb.append(String.format("Text 1 Tokens (%d): %s\n", 
            processedTokens1.size(), processedTokens1));
        sb.append(String.format("Text 2 Tokens (%d): %s\n", 
            processedTokens2.size(), processedTokens2));
        
        if (featureVectors != null) {
            sb.append("\n--- Feature Extraction Results ---\n");
            sb.append(String.format("Feature Vector 1 Size: %d\n", 
                featureVectors[0].size()));
            sb.append(String.format("Feature Vector 2 Size: %d\n", 
                featureVectors[1].size()));
            sb.append(String.format("Feature Extractor Type: %s\n", 
                featureVectors[0].getExtractorType()));
        }
        
        return sb.toString();
    }
}