package org.yapp.core.similarity.feature;

import java.util.*;

/**
 * TF (Term Frequency) 특징 추출기
 * IDF를 1로 처리하여 순수 TF 값만으로 특징 벡터를 생성
 */
public class TfExtractor implements FeatureExtractor {
    
    @Override
    public FeatureVector[] extractFeatures(List<String> tokens1, List<String> tokens2) {
        if (tokens1.isEmpty() && tokens2.isEmpty()) {
            return new FeatureVector[]{
                new FeatureVector(Map.of(), getType()),
                new FeatureVector(Map.of(), getType())
            };
        }
        
        // TF 계산
        Map<String, Double> tf1 = calculateTF(tokens1);
        Map<String, Double> tf2 = calculateTF(tokens2);
        
        return new FeatureVector[]{
            new FeatureVector(tf1, getType()),
            new FeatureVector(tf2, getType())
        };
    }
    
    private Map<String, Double> calculateTF(List<String> tokens) {
        Map<String, Integer> frequency = new HashMap<>();
        Map<String, Double> tf = new HashMap<>();
        
        // 빈도 계산
        for (String token : tokens) {
            frequency.put(token, frequency.getOrDefault(token, 0) + 1);
        }
        
        // 정규화된 TF 계산
        double totalTokens = tokens.size();
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            tf.put(entry.getKey(), entry.getValue() / totalTokens);
        }
        
        return tf;
    }
    
    @Override
    public String getType() {
        return "TF";
    }
}