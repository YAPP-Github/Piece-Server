package org.yapp.core.similarity.feature;

import java.util.List;

/**
 * N개 문서를 지원하는 특징 추출 인터페이스
 * 여러 문서의 토큰 리스트들로부터 특징 벡터를 추출하는 역할
 */
public interface MultiDocumentFeatureExtractor {
    
    /**
     * N개의 토큰 리스트로부터 특징 벡터들을 추출합니다.
     * 
     * @param documentsTokens 문서별 토큰 리스트들
     * @return 각 문서의 특징 벡터 배열
     */
    FeatureVector[] extractFeatures(List<List<String>> documentsTokens);
    
    /**
     * 특징 추출기의 타입을 반환합니다.
     * 
     * @return 추출기 타입
     */
    String getType();
    
    /**
     * 2개 문서용 인터페이스와의 호환성을 위한 메서드
     * 
     * @param tokens1 첫 번째 토큰 리스트
     * @param tokens2 두 번째 토큰 리스트
     * @return 두 개의 특징 벡터 배열
     */
    default FeatureVector[] extractFeatures(List<String> tokens1, List<String> tokens2) {
        return extractFeatures(List.of(tokens1, tokens2));
    }
}