package org.yapp.core.similarity.calculator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yapp.core.similarity.feature.FeatureVector;

/**
 * N개 문서를 지원하는 코사인 유사도 계산기 모든 문서 쌍 간의 코사인 유사도를 계산
 */
public class MultiDocumentCosineSimilarityCalculator implements MultiDocumentSimilarityCalculator {

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
                double similarity = calculateCosineSimilarity(
                    vectors[i].getFeatures(),
                    vectors[j].getFeatures()
                );
                matrix[i][j] = similarity;
                matrix[j][i] = similarity; // 대칭 행렬
            }
        }

        return matrix;
    }

    @Override
    public double[][] calculateSimilarityMatrix(List<List<String>> documentsTokens) {
        throw new UnsupportedOperationException(
            "CosineSimilarity requires feature vectors. Use FeatureExtractor first.");
    }

    @Override
    public double[] calculateSimilarityToTarget(int targetIndex, FeatureVector[] vectors) {
        if (targetIndex < 0 || targetIndex >= vectors.length) {
            throw new IllegalArgumentException("Invalid target index: " + targetIndex);
        }

        double[] similarities = new double[vectors.length];
        FeatureVector targetVector = vectors[targetIndex];

        for (int i = 0; i < vectors.length; i++) {
            if (i == targetIndex) {
                similarities[i] = 1.0; // 자기 자신과의 유사도
            } else {
                similarities[i] = calculateCosineSimilarity(
                    targetVector.getFeatures(),
                    vectors[i].getFeatures()
                );
            }
        }

        return similarities;
    }

    private double calculateCosineSimilarity(Map<String, Double> vector1,
        Map<String, Double> vector2) {
        if (vector1.isEmpty() && vector2.isEmpty()) {
            return 1.0;
        }

        if (vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }

        Set<String> allTerms = new HashSet<>(vector1.keySet());
        allTerms.addAll(vector2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String term : allTerms) {
            double value1 = vector1.getOrDefault(term, 0.0);
            double value2 = vector2.getOrDefault(term, 0.0);

            dotProduct += value1 * value2;
            norm1 += value1 * value1;
            norm2 += value2 * value2;
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @Override
    public String getType() {
        return "MULTI_COSINE";
    }

    @Override
    public boolean requiresFeatureVector() {
        return true;
    }
}