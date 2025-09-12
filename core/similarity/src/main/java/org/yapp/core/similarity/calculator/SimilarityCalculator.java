package org.yapp.core.similarity.calculator;

import org.yapp.core.similarity.feature.FeatureVector;
import java.util.List;

/**
 * 유사도 계산 인터페이스
 * 특징 벡터 또는 토큰 리스트를 기반으로 유사도를 계산
 */
public interface SimilarityCalculator {
    
    /**
     * 특징 벡터를 기반으로 유사도를 계산합니다.
     * 
     * @param vector1 첫 번째 특징 벡터
     * @param vector2 두 번째 특징 벡터
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    double calculate(FeatureVector vector1, FeatureVector vector2);
    
    /**
     * 토큰 리스트를 기반으로 직접 유사도를 계산합니다.
     * (특징 추출이 필요없는 경우 사용)
     * 
     * @param tokens1 첫 번째 토큰 리스트
     * @param tokens2 두 번째 토큰 리스트
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    double calculate(List<String> tokens1, List<String> tokens2);
    
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
}