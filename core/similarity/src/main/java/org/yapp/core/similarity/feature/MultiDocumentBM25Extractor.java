package org.yapp.core.similarity.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * N개 문서를 지원하는 Okapi BM25 특징 추출기 여러 문서의 BM25 벡터를 동시에 계산하여 일관된 IDF와 평균 문서 길이를 사용
 */
public class MultiDocumentBM25Extractor implements MultiDocumentFeatureExtractor {

    // BM25 파라미터
    private static final double K1 = 1.5; // 항목 빈도 포화 파라미터
    private static final double B = 0.75;  // 문서 길이 정규화 파라미터

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

        // 문서 길이들 계산
        List<Double> docLengths = documentsTokens.stream()
            .map(List::size)
            .map(Double::valueOf)
            .collect(Collectors.toList());

        // 평균 문서 길이 계산
        double avgDocLength = docLengths.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElse(0.0);

        // 각 문서의 항목 빈도 계산
        List<Map<String, Integer>> tfList = new ArrayList<>();
        for (List<String> tokens : documentsTokens) {
            tfList.add(calculateTermFrequency(tokens));
        }

        // 전체 문서 컬렉션에 대한 IDF 계산
        Map<String, Double> idf = calculateIDF(documentsTokens);

        // 각 문서의 BM25 벡터 계산
        FeatureVector[] results = new FeatureVector[documentsTokens.size()];
        for (int i = 0; i < documentsTokens.size(); i++) {
            Map<String, Double> bm25 = calculateBM25(
                tfList.get(i),
                idf,
                docLengths.get(i),
                avgDocLength
            );
            results[i] = new FeatureVector(bm25, getType());
        }

        return results;
    }

    private Map<String, Integer> calculateTermFrequency(List<String> tokens) {
        Map<String, Integer> tf = new HashMap<>();
        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0) + 1);
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

            // BM25 IDF = log((N - df + 0.5) / (df + 0.5))
            // 여기서 N은 전체 문서 수, df는 해당 용어를 포함한 문서 수
            double idfValue = Math.log(
                (totalDocuments - documentsContainingToken + 0.5) /
                    (documentsContainingToken + 0.5)
            );
            idf.put(token, Math.max(0.0, idfValue)); // 음수 방지
        }

        return idf;
    }

    private Map<String, Double> calculateBM25(Map<String, Integer> tf,
        Map<String, Double> idf,
        double docLength,
        double avgDocLength) {
        Map<String, Double> bm25 = new HashMap<>();

        for (Map.Entry<String, Integer> entry : tf.entrySet()) {
            String term = entry.getKey();
            int termFreq = entry.getValue();
            double idfValue = idf.getOrDefault(term, 0.0);

            // BM25 공식: IDF * (tf * (k1 + 1)) / (tf + k1 * (1 - b + b * |d| / avgdl))
            double numerator = termFreq * (K1 + 1);
            double denominator = termFreq + K1 * (1 - B + B * (docLength / avgDocLength));
            double bm25Score = idfValue * (numerator / denominator);

            bm25.put(term, bm25Score);
        }

        return bm25;
    }

    @Override
    public String getType() {
        return "MULTI_BM25";
    }
}