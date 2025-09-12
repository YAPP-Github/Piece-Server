package org.yapp.core.similarity.pipeline;

import org.yapp.core.similarity.feature.FeatureVector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * N개 문서 유사도 계산 결과를 담는 클래스
 * 유사도 매트릭스와 함께 처리 과정의 상세 정보를 포함
 */
public class MultiDocumentSimilarityResult {
    
    private final double[][] similarityMatrix;
    private final List<List<String>> processedTokensPerDocument;
    private final FeatureVector[] featureVectors;
    private final long processingTimeNanos;
    private final String pipelineInfo;
    
    public MultiDocumentSimilarityResult(double[][] similarityMatrix,
                                       List<List<String>> processedTokensPerDocument,
                                       FeatureVector[] featureVectors,
                                       long processingTimeNanos,
                                       String pipelineInfo) {
        this.similarityMatrix = cloneMatrix(similarityMatrix);
        this.processedTokensPerDocument = processedTokensPerDocument.stream()
            .map(List::copyOf)
            .collect(Collectors.toList());
        this.featureVectors = featureVectors;
        this.processingTimeNanos = processingTimeNanos;
        this.pipelineInfo = pipelineInfo;
    }
    
    /**
     * 유사도 매트릭스를 반환합니다.
     * 
     * @return N×N 유사도 매트릭스
     */
    public double[][] getSimilarityMatrix() {
        return cloneMatrix(similarityMatrix);
    }
    
    /**
     * 특정 문서 쌍의 유사도를 반환합니다.
     * 
     * @param i 첫 번째 문서 인덱스
     * @param j 두 번째 문서 인덱스
     * @return 두 문서 간의 유사도
     */
    public double getSimilarity(int i, int j) {
        if (i < 0 || i >= similarityMatrix.length || j < 0 || j >= similarityMatrix[0].length) {
            throw new IllegalArgumentException("Invalid document indices: " + i + ", " + j);
        }
        return similarityMatrix[i][j];
    }
    
    /**
     * 특정 문서와 다른 모든 문서들 간의 유사도를 반환합니다.
     * 
     * @param documentIndex 대상 문서 인덱스
     * @return 대상 문서와 다른 문서들 간의 유사도 배열
     */
    public double[] getSimilaritiesToDocument(int documentIndex) {
        if (documentIndex < 0 || documentIndex >= similarityMatrix.length) {
            throw new IllegalArgumentException("Invalid document index: " + documentIndex);
        }
        return similarityMatrix[documentIndex].clone();
    }
    
    /**
     * 각 문서의 전처리된 토큰들을 반환합니다.
     * 
     * @return 문서별 전처리된 토큰 리스트들
     */
    public List<List<String>> getProcessedTokensPerDocument() {
        return processedTokensPerDocument;
    }
    
    /**
     * 특정 문서의 전처리된 토큰들을 반환합니다.
     * 
     * @param documentIndex 문서 인덱스
     * @return 해당 문서의 전처리된 토큰 리스트
     */
    public List<String> getProcessedTokens(int documentIndex) {
        if (documentIndex < 0 || documentIndex >= processedTokensPerDocument.size()) {
            throw new IllegalArgumentException("Invalid document index: " + documentIndex);
        }
        return processedTokensPerDocument.get(documentIndex);
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
     * 문서 개수를 반환합니다.
     * 
     * @return 문서 개수
     */
    public int getDocumentCount() {
        return similarityMatrix.length;
    }
    
    /**
     * 상세 분석 결과를 문자열로 반환합니다.
     * 
     * @return 상세 분석 결과
     */
    public String getDetailedAnalysis() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Multi-Document Similarity Analysis Result ===\n");
        sb.append(String.format("Pipeline: %s\n", pipelineInfo));
        sb.append(String.format("Document Count: %d\n", getDocumentCount()));
        sb.append(String.format("Processing Time: %.2f ms\n", getProcessingTimeMillis()));
        
        sb.append("\n--- Similarity Matrix ---\n");
        for (int i = 0; i < similarityMatrix.length; i++) {
            sb.append(String.format("Doc %d: [", i));
            for (int j = 0; j < similarityMatrix[i].length; j++) {
                sb.append(String.format("%.3f", similarityMatrix[i][j]));
                if (j < similarityMatrix[i].length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        
        sb.append("\n--- Preprocessing Results ---\n");
        for (int i = 0; i < processedTokensPerDocument.size(); i++) {
            List<String> tokens = processedTokensPerDocument.get(i);
            sb.append(String.format("Doc %d Tokens (%d): %s\n", 
                i, tokens.size(), tokens));
        }
        
        if (featureVectors != null) {
            sb.append("\n--- Feature Extraction Results ---\n");
            for (int i = 0; i < featureVectors.length; i++) {
                sb.append(String.format("Doc %d Feature Vector Size: %d\n", 
                    i, featureVectors[i].size()));
            }
            if (featureVectors.length > 0) {
                sb.append(String.format("Feature Extractor Type: %s\n", 
                    featureVectors[0].getExtractorType()));
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 매트릭스를 복사하는 유틸리티 메서드
     */
    private double[][] cloneMatrix(double[][] matrix) {
        if (matrix == null) return null;
        double[][] clone = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            clone[i] = matrix[i].clone();
        }
        return clone;
    }
}