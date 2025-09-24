package org.yapp.core.similarity.feature;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BM25Extractor implements FeatureExtractor {

    // BM25 파라미터
    private static final double K1 = 1.5; // 항목 빈도 포화 파라미터
    private static final double B = 0.75;  // 문서 길이 정규화 파라미터

    @Override
    public FeatureVector[] extractFeatures(List<String> tokens1, List<String> tokens2) {
        if (tokens1.isEmpty() && tokens2.isEmpty()) {
            return new FeatureVector[]{
                new FeatureVector(Map.of(), getType()),
                new FeatureVector(Map.of(), getType())
            };
        }

        // 문서 길이 계산
        double docLength1 = tokens1.size();
        double docLength2 = tokens2.size();
        double avgDocLength = (docLength1 + docLength2) / 2.0;

        // 항목 빈도 계산
        Map<String, Integer> tf1 = calculateTermFrequency(tokens1);
        Map<String, Integer> tf2 = calculateTermFrequency(tokens2);

        // IDF 계산
        Map<String, Double> idf = calculateIDF(tokens1, tokens2);

        // BM25 점수 계산
        Map<String, Double> bm25_1 = calculateBM25(tf1, idf, docLength1, avgDocLength);
        Map<String, Double> bm25_2 = calculateBM25(tf2, idf, docLength2, avgDocLength);

        return new FeatureVector[]{
            new FeatureVector(bm25_1, getType()),
            new FeatureVector(bm25_2, getType())
        };
    }

    private Map<String, Integer> calculateTermFrequency(List<String> tokens) {
        Map<String, Integer> tf = new HashMap<>();
        for (String token : tokens) {
            tf.put(token, tf.getOrDefault(token, 0) + 1);
        }
        return tf;
    }

    private Map<String, Double> calculateIDF(List<String> tokens1, List<String> tokens2) {
        Map<String, Double> idf = new HashMap<>();

        Set<String> uniqueTokens1 = new HashSet<>(tokens1);
        Set<String> uniqueTokens2 = new HashSet<>(tokens2);
        Set<String> allTokens = new HashSet<>(uniqueTokens1);
        allTokens.addAll(uniqueTokens2);

        double totalDocuments = 2.0;

        for (String token : allTokens) {
            int documentsContainingToken = 0;
            if (uniqueTokens1.contains(token)) {
                documentsContainingToken++;
            }
            if (uniqueTokens2.contains(token)) {
                documentsContainingToken++;
            }

            // IDF = log((N - df + 0.5) / (df + 0.5))
            // 여기서 N은 전체 문서 수, df는 해당 용어를 포함한 문서 수
            // 스무딩을 적용하여 1을 더해줌
            double idfValue = Math.log(
                1 + (totalDocuments - documentsContainingToken + 0.5) /
                    (documentsContainingToken + 0.5)
            );

            idf.put(token, idfValue);
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
        return "BM25";
    }
}