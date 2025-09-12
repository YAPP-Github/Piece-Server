package org.yapp.core.similarity.calculator;

import org.yapp.core.similarity.feature.FeatureVector;
import java.util.List;

/**
 * N개 문서를 지원하는 유사도 계산 인터페이스
 * 여러 문서들 간의 유사도 매트릭스를 계산
 */
public interface MultiDocumentSimilarityCalculator {
    
    /**
     * N개의 특징 벡터들 간의 유사도 매트릭스를 계산합니다.
     * 
     * @param vectors 특징 벡터 배열
     * @return N×N 유사도 매트릭스 (대각선은 1.0)
     */
    double[][] calculateSimilarityMatrix(FeatureVector[] vectors);
    
    /**
     * N개의 토큰 리스트들 간의 유사도 매트릭스를 직접 계산합니다.
     * (특징 추출이 필요없는 경우 사용)
     * 
     * @param documentsTokens 문서별 토큰 리스트들
     * @return N×N 유사도 매트릭스 (대각선은 1.0)
     */
    double[][] calculateSimilarityMatrix(List<List<String>> documentsTokens);
    
    /**
     * 특정 문서와 다른 모든 문서들 간의 유사도를 계산합니다.
     * 
     * @param targetIndex 대상 문서의 인덱스
     * @param vectors 모든 특징 벡터들
     * @return 대상 문서와 다른 문서들 간의 유사도 배열
     */
    double[] calculateSimilarityToTarget(int targetIndex, FeatureVector[] vectors);
    
    /**
     * 유사도 계산기의 타입을 반환합니다.
     * 
     * @return 계산기 타입
     */
    String getType();
    
    /**
     * 이 계산기가 특징 벡터를 필요로 하는지 여부를 반환합니다.
     * 
     * @return 특징 벡터가 필요하면 true, 토큰 리스트만으로 충분하면 false
     */
    boolean requiresFeatureVector();
    
    /**
     * 2개 문서용 인터페이스와의 호환성을 위한 메서드
     * 
     * @param vector1 첫 번째 특징 벡터
     * @param vector2 두 번째 특징 벡터
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    default double calculate(FeatureVector vector1, FeatureVector vector2) {
        double[][] matrix = calculateSimilarityMatrix(new FeatureVector[]{vector1, vector2});
        return matrix[0][1];
    }
    
    /**
     * 2개 문서용 인터페이스와의 호환성을 위한 메서드
     * 
     * @param tokens1 첫 번째 토큰 리스트
     * @param tokens2 두 번째 토큰 리스트
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    default double calculate(List<String> tokens1, List<String> tokens2) {
        double[][] matrix = calculateSimilarityMatrix(List.of(tokens1, tokens2));
        return matrix[0][1];
    }
}