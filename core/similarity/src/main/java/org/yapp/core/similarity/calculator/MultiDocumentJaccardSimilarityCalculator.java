package org.yapp.core.similarity.calculator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.yapp.core.similarity.feature.FeatureVector;

/**
 * N개 문서를 지원하는 자카드 유사도 계산기 모든 문서 쌍 간의 자카드 유사도를 계산
 */
public class MultiDocumentJaccardSimilarityCalculator implements MultiDocumentSimilarityCalculator {

    @Override
    public double[][] calculateSimilarityMatrix(FeatureVector[] vectors) {
        int n = vectors.length;
        double[][] matrix = new double[n][n];

        // 대각선은 1.0 (자기 자신과의 유사도)
        for (int i = 0; i < n; i++) {
            matrix[i][i] = 1.0;
        }

        // 상삼각 행렬 계산 (대칭 행렬이므로)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Set<String> set1 = vectors[i].getFeatures().keySet();
                Set<String> set2 = vectors[j].getFeatures().keySet();
                double similarity = calculateJaccardSimilarity(set1, set2);
                matrix[i][j] = similarity;
                matrix[j][i] = similarity; // 대칭 행렬
            }
        }

        return matrix;
    }

    @Override
    public double[][] calculateSimilarityMatrix(List<List<String>> documentsTokens) {
        int n = documentsTokens.size();
        double[][] matrix = new double[n][n];

        // 토큰 리스트를 집합으로 변환
        List<Set<String>> sets = documentsTokens.stream()
            .map(HashSet::new)
            .collect(Collectors.toList());

        // 대각선은 1.0 (자기 자신과의 유사도)
        for (int i = 0; i < n; i++) {
            matrix[i][i] = 1.0;
        }

        // 상삼각 행렬 계산 (대칭 행렬이므로)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double similarity = calculateJaccardSimilarity(sets.get(i), sets.get(j));
                matrix[i][j] = similarity;
                matrix[j][i] = similarity; // 대칭 행렬
            }
        }

        return matrix;
    }

    @Override
    public double[] calculateSimilarityToTarget(int targetIndex, FeatureVector[] vectors) {
        if (targetIndex < 0 || targetIndex >= vectors.length) {
            throw new IllegalArgumentException("Invalid target index: " + targetIndex);
        }

        double[] similarities = new double[vectors.length];
        Set<String> targetSet = vectors[targetIndex].getFeatures().keySet();

        for (int i = 0; i < vectors.length; i++) {
            if (i == targetIndex) {
                similarities[i] = 1.0; // 자기 자신과의 유사도
            } else {
                Set<String> compareSet = vectors[i].getFeatures().keySet();
                similarities[i] = calculateJaccardSimilarity(targetSet, compareSet);
            }
        }

        return similarities;
    }

    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 1.0;
        }

        if (set1.isEmpty() || set2.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    @Override
    public String getType() {
        return "MULTI_JACCARD";
    }

    @Override
    public boolean requiresFeatureVector() {
        return false; // 토큰 리스트만으로도 계산 가능
    }
}