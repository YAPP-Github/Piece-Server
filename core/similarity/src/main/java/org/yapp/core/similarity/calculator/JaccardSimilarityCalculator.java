package org.yapp.core.similarity.calculator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.yapp.core.similarity.feature.FeatureVector;

/**
 * 자카드 유사도 계산기 두 집합 간의 자카드 유사도를 계산 Jaccard Similarity = |A ∩ B| / |A ∪ B|
 */
public class JaccardSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculate(FeatureVector vector1, FeatureVector vector2) {
        throw new UnsupportedOperationException(
            "Jaccard similarity should use token lists directly, not feature vectors. Use calculate(List<String>, List<String>) instead.");
    }


    @Override
    public double calculate(List<String> tokens1, List<String> tokens2) {
        // 토큰 리스트를 집합으로 변환하여 중복 제거
        Set<String> set1 = new HashSet<>(tokens1);
        Set<String> set2 = new HashSet<>(tokens2);

        return calculateJaccardSimilarity(set1, set2);
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
        return "JACCARD";
    }

    @Override
    public boolean requiresFeatureVector() {
        return false; // 토큰 리스트만으로도 계산 가능
    }
}