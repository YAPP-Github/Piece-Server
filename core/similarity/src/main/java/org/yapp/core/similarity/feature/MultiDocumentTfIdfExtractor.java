package org.yapp.core.similarity.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * N개 문서를 지원하는 TF-IDF 특징 추출기 여러 문서의 TF-IDF 벡터를 동시에 계산하여 일관된 IDF 값을 사용
 */
public class MultiDocumentTfIdfExtractor implements MultiDocumentFeatureExtractor {

    @Override
    public FeatureVector[] extractFeatures(List<List<String>> documentsTokens) {
        if (documentsTokens == null || documentsTokens.isEmpty()) {
            return new FeatureVector[0];
        }

        // 모든 문서가 비어있는 경우
        if (documentsTokens.stream().allMatch(List::isEmpty)) {
            return documentsTokens.stream()
                .map(tokens -> new FeatureVector(Map.of(), getType()))
                .toArray(FeatureVector[]::new);
        }

        // 각 문서의 TF 계산
        List<Map<String, Double>> tfList = new ArrayList<>();
        for (List<String> tokens : documentsTokens) {
            tfList.add(calculateTF(tokens));
        }

        // 전체 문서 컬렉션에 대한 IDF 계산
        Map<String, Double> idf = calculateIDF(documentsTokens);

        // 각 문서의 TF-IDF 벡터 계산
        FeatureVector[] results = new FeatureVector[documentsTokens.size()];
        for (int i = 0; i < documentsTokens.size(); i++) {
            Map<String, Double> tfidf = calculateTFIDF(tfList.get(i), idf);
            results[i] = new FeatureVector(tfidf, getType());
        }

        return results;
    }

    private Map<String, Double> calculateTF(List<String> tokens) {
        if (tokens.isEmpty()) {
            return Map.of();
        }

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

    private Map<String, Double> calculateIDF(List<List<String>> documentsTokens) {
        Map<String, Double> idf = new HashMap<>();

        // 각 문서의 고유 토큰 집합 생성
        List<Set<String>> uniqueTokensList = documentsTokens.stream()
            .map(HashSet::new)
            .collect(Collectors.toList());

        // 모든 토큰 수집
        Set<String> allTokens = new HashSet<>();
        for (Set<String> uniqueTokens : uniqueTokensList) {
            allTokens.addAll(uniqueTokens);
        }

        double totalDocuments = documentsTokens.size() + 1.0; // 스무딩 적용

        for (String token : allTokens) {
            long documentsContainingToken = uniqueTokensList.stream()
                .mapToLong(uniqueTokens -> uniqueTokens.contains(token) ? 1 : 0)
                .sum();

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
        return "MULTI_TF_IDF";
    }
}