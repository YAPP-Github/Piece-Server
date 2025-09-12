package org.yapp.core.similarity.feature;

import java.util.*;

/**
 * TF-IDF 특징 추출기
 * Term Frequency - Inverse Document Frequency 기반으로 특징 벡터를 생성
 */
public class TfIdfExtractor implements FeatureExtractor {
    
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
        
        // IDF 계산
        Map<String, Double> idf = calculateIDF(tokens1, tokens2);
        
        // TF-IDF 벡터 계산
        Map<String, Double> tfidf1 = calculateTFIDF(tf1, idf);
        Map<String, Double> tfidf2 = calculateTFIDF(tf2, idf);
        
        return new FeatureVector[]{
            new FeatureVector(tfidf1, getType()),
            new FeatureVector(tfidf2, getType())
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
    
    private Map<String, Double> calculateIDF(List<String> tokens1, List<String> tokens2) {
        Map<String, Double> idf = new HashMap<>();
        
        // 고유 토큰 집합 생성
        Set<String> uniqueTokens1 = new HashSet<>(tokens1);
        Set<String> uniqueTokens2 = new HashSet<>(tokens2);
        Set<String> allTokens = new HashSet<>(uniqueTokens1);
        allTokens.addAll(uniqueTokens2);
        
        double totalDocuments = 2.0 + 1.0; // 스무딩 적용
        
        for (String token : allTokens) {
            int documentsContainingToken = 0;
            if (uniqueTokens1.contains(token)) {
                documentsContainingToken++;
            }
            if (uniqueTokens2.contains(token)) {
                documentsContainingToken++;
            }
            
            // IDF = log(총 문서수 / 해당 용어를 포함한 문서수)
            double idfValue = Math.log(totalDocuments / documentsContainingToken);
            idf.put(token, idfValue);
        }
        
        return idf;
    }
    
    private Map<String, Double> calculateTFIDF(Map<String, Double> tf, Map<String, Double> idf) {
        Map<String, Double> tfidf = new HashMap<>();
        
        for (String token : tf.keySet()) {
            double tfValue = tf.get(token);
            double idfValue = idf.getOrDefault(token, 0.0);
            tfidf.put(token, tfValue * idfValue);
        }
        
        return tfidf;
    }
    
    @Override
    public String getType() {
        return "TF_IDF";
    }
}