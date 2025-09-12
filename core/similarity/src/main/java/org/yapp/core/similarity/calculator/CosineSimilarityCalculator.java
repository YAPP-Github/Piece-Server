package org.yapp.core.similarity.calculator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yapp.core.similarity.feature.FeatureVector;

/**
 * 코사인 유사도 계산기 두 벡터 간의 코사인 유사도를 계산
 */
public class CosineSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculate(FeatureVector vector1, FeatureVector vector2) {
        if (vector1.isEmpty() && vector2.isEmpty()) {
            return 1.0;
        }

        if (vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }

        Map<String, Double> features1 = vector1.getFeatures();
        Map<String, Double> features2 = vector2.getFeatures();

        return calculateCosineSimilarity(features1, features2);
    }

    @Override
    public double calculate(List<String> tokens1, List<String> tokens2) {
        throw new UnsupportedOperationException(
            "CosineSimilarity requires feature vectors. Use FeatureExtractor first.");
    }

    private double calculateCosineSimilarity(Map<String, Double> vector1,
        Map<String, Double> vector2) {
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
        return "COSINE";
    }

    @Override
    public boolean requiresFeatureVector() {
        return true;
    }
}