package org.yapp.core.similarity.feature;

import java.util.List;

/**
 * 특징 추출 인터페이스
 * 토큰 리스트들로부터 특징 벡터를 추출하는 역할
 */
public interface FeatureExtractor {
    
    /**
     * 두 토큰 리스트로부터 특징 벡터들을 추출합니다.
     * 
     * @param tokens1 첫 번째 토큰 리스트
     * @param tokens2 두 번째 토큰 리스트
     * @return 두 개의 특징 벡터 배열 [vector1, vector2]
     */
    FeatureVector[] extractFeatures(List<String> tokens1, List<String> tokens2);
    
    /**
     * 특징 추출기의 타입을 반환합니다.
     * 
     * @return 추출기 타입
     */
    String getType();
}