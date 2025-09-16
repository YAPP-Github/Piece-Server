package org.yapp.core.similarity.feature;

import java.util.Map;

/**
 * 특징 벡터를 나타내는 클래스
 * 토큰과 해당 가중치(weight)를 맵으로 저장
 */
public class FeatureVector {
    
    private final Map<String, Double> features;
    private final String extractorType;
    
    public FeatureVector(Map<String, Double> features, String extractorType) {
        this.features = Map.copyOf(features);
        this.extractorType = extractorType;
    }
    
    /**
     * 특징 맵을 반환합니다.
     * 
     * @return 토큰과 가중치의 맵
     */
    public Map<String, Double> getFeatures() {
        return features;
    }
    
    /**
     * 특정 토큰의 가중치를 반환합니다.
     * 
     * @param token 가중치를 조회할 토큰
     * @return 토큰의 가중치 (존재하지 않으면 0.0)
     */
    public double getWeight(String token) {
        return features.getOrDefault(token, 0.0);
    }
    
    /**
     * 특징 추출기 타입을 반환합니다.
     * 
     * @return 추출기 타입
     */
    public String getExtractorType() {
        return extractorType;
    }
    
    /**
     * 벡터의 크기(차원)를 반환합니다.
     * 
     * @return 벡터 크기
     */
    public int size() {
        return features.size();
    }
    
    /**
     * 벡터가 비어있는지 확인합니다.
     * 
     * @return 비어있으면 true
     */
    public boolean isEmpty() {
        return features.isEmpty();
    }
}