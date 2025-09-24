package org.yapp.core.similarity.pipeline;

import org.yapp.core.similarity.calculator.MultiDocumentSimilarityCalculator;
import org.yapp.core.similarity.feature.FeatureVector;
import org.yapp.core.similarity.feature.MultiDocumentFeatureExtractor;
import org.yapp.core.similarity.preprocessing.TextPreprocessor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * N개 문서를 지원하는 유사도 검사 파이프라인
 * 전처리 → 특징 추출 → 유사도 계산의 3단계를 N개 문서에 대해 수행
 */
public class MultiDocumentSimilarityPipeline {
    
    private final TextPreprocessor preprocessor;
    private final MultiDocumentFeatureExtractor featureExtractor;
    private final MultiDocumentSimilarityCalculator similarityCalculator;
    
    public MultiDocumentSimilarityPipeline(TextPreprocessor preprocessor,
                                         MultiDocumentFeatureExtractor featureExtractor,
                                         MultiDocumentSimilarityCalculator similarityCalculator) {
        if (preprocessor == null) {
            throw new IllegalArgumentException("TextPreprocessor cannot be null");
        }
        if (similarityCalculator == null) {
            throw new IllegalArgumentException("MultiDocumentSimilarityCalculator cannot be null");
        }
        if (similarityCalculator.requiresFeatureVector() && featureExtractor == null) {
            throw new IllegalArgumentException("FeatureExtractor cannot be null when SimilarityCalculator requires feature vectors");
        }
        
        this.preprocessor = preprocessor;
        this.featureExtractor = featureExtractor;
        this.similarityCalculator = similarityCalculator;
    }
    
    /**
     * N개 문서들 간의 유사도 매트릭스를 계산합니다.
     * 
     * @param documents 문서 텍스트들
     * @return N×N 유사도 매트릭스
     */
    public double[][] calculateSimilarityMatrix(List<String> documents) {
        if (documents == null || documents.isEmpty()) {
            return new double[0][0];
        }
        
        // 단일 문서인 경우
        if (documents.size() == 1) {
            return new double[][]{{1.0}};
        }
        
        // 1단계: 전처리
        List<List<String>> documentsTokens = documents.stream()
            .map(doc -> doc == null ? List.<String>of() : preprocessor.preprocess(doc))
            .collect(Collectors.toList());
        
        // 계산기가 특징 벡터를 필요로 하는지 확인
        if (similarityCalculator.requiresFeatureVector()) {
            // 2단계: 특징 추출
            FeatureVector[] features = featureExtractor.extractFeatures(documentsTokens);
            
            // 3단계: 유사도 계산 (특징 벡터 사용)
            return similarityCalculator.calculateSimilarityMatrix(features);
        } else {
            // 3단계: 유사도 계산 (토큰 리스트 직접 사용)
            return similarityCalculator.calculateSimilarityMatrix(documentsTokens);
        }
    }
    
    /**
     * 특정 문서와 다른 모든 문서들 간의 유사도를 계산합니다.
     * 
     * @param documents 모든 문서 텍스트들
     * @param targetIndex 대상 문서의 인덱스
     * @return 대상 문서와 다른 문서들 간의 유사도 배열
     */
    public double[] calculateSimilarityToTarget(List<String> documents, int targetIndex) {
        if (documents == null || documents.isEmpty()) {
            return new double[0];
        }
        
        if (targetIndex < 0 || targetIndex >= documents.size()) {
            throw new IllegalArgumentException("Invalid target index: " + targetIndex);
        }
        
        // 단일 문서인 경우
        if (documents.size() == 1) {
            return new double[]{1.0};
        }
        
        // 1단계: 전처리
        List<List<String>> documentsTokens = documents.stream()
            .map(doc -> doc == null ? List.<String>of() : preprocessor.preprocess(doc))
            .collect(Collectors.toList());
        
        // 계산기가 특징 벡터를 필요로 하는지 확인
        if (similarityCalculator.requiresFeatureVector()) {
            // 2단계: 특징 추출
            FeatureVector[] features = featureExtractor.extractFeatures(documentsTokens);
            
            // 3단계: 대상 문서와의 유사도 계산
            return similarityCalculator.calculateSimilarityToTarget(targetIndex, features);
        } else {
            // 전체 매트릭스를 계산한 후 해당 행 추출 (비효율적이지만 단순함)
            double[][] matrix = similarityCalculator.calculateSimilarityMatrix(documentsTokens);
            return matrix[targetIndex];
        }
    }
    
    /**
     * N개 문서들 간의 유사도 계산과 함께 상세 정보를 반환합니다.
     * 
     * @param documents 문서 텍스트들
     * @return 상세 분석 결과
     */
    public MultiDocumentSimilarityResult calculateSimilarityMatrixWithDetails(List<String> documents) {
        long startTime = System.nanoTime();
        
        if (documents == null || documents.isEmpty()) {
            return new MultiDocumentSimilarityResult(
                new double[0][0],
                List.of(),
                null,
                System.nanoTime() - startTime,
                getPipelineInfo()
            );
        }
        
        // 전처리
        List<List<String>> documentsTokens = documents.stream()
            .map(doc -> doc == null ? List.<String>of() : preprocessor.preprocess(doc))
            .collect(Collectors.toList());
        
        double[][] similarityMatrix;
        FeatureVector[] features = null;
        
        if (similarityCalculator.requiresFeatureVector()) {
            features = featureExtractor.extractFeatures(documentsTokens);
            similarityMatrix = similarityCalculator.calculateSimilarityMatrix(features);
        } else {
            similarityMatrix = similarityCalculator.calculateSimilarityMatrix(documentsTokens);
        }
        
        long endTime = System.nanoTime();
        long processingTimeNanos = endTime - startTime;
        
        return new MultiDocumentSimilarityResult(
            similarityMatrix,
            documentsTokens,
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
        return String.format("MultiDocumentPipeline[%s → %s → %s]",
            preprocessor.getPreprocessorInfo(),
            featureInfo,
            similarityCalculator.getType());
    }
}