package org.yapp.core.similarity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.yapp.core.similarity.calculator.CosineSimilarityCalculator;
import org.yapp.core.similarity.calculator.JaccardSimilarityCalculator;
import org.yapp.core.similarity.feature.BM25Extractor;
import org.yapp.core.similarity.feature.TfExtractor;
import org.yapp.core.similarity.feature.TfIdfExtractor;
import org.yapp.core.similarity.pipeline.SimilarityPipeline;
import org.yapp.core.similarity.preprocessing.TextPreprocessor;
import org.yapp.core.similarity.preprocessing.cleaner.KoreanTextCleaner;
import org.yapp.core.similarity.preprocessing.remover.KoreanStopWordRemover;
import org.yapp.core.similarity.preprocessing.tokenizer.KomoranTokenizer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentSimilarityUtils {

    private static final SimilarityPipeline KOREAN_JACCARD_PIPELINE = createKoreanJaccardPipeline();
    private static final SimilarityPipeline KOREAN_TF_COSINE_PIPELINE = createKoreanTfCosinePipeline();
    private static final SimilarityPipeline KOREAN_TFIDF_COSINE_PIPELINE = createKoreanTfIdfCosinePipeline();
    private static final SimilarityPipeline KOREAN_BM25_COSINE_PIPELINE = createKoreanBM25CosinePipeline();

    /**
     * 기본 한국어 유사도 계산 (한국어 전처리 + TF + Cosine 유사도)
     *
     * @param text1 첫 번째 텍스트
     * @param text2 두 번째 텍스트
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    public static double calculateSimilarity(String text1, String text2) {
        return calculateWithTfCosine(text1, text2);
    }

    public static double calculateWithTfCosine(String text1, String text2) {
        return KOREAN_TF_COSINE_PIPELINE.calculateSimilarity(text1, text2);
    }

    /**
     * 한국어 TF-IDF + 코사인 유사도 계산
     *
     * @param text1 첫 번째 텍스트
     * @param text2 두 번째 텍스트
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    public static double calculateWithTfIdfCosine(String text1, String text2) {
        return KOREAN_TFIDF_COSINE_PIPELINE.calculateSimilarity(text1, text2);
    }

    /**
     * 한국어 BM25 + 코사인 유사도 계산
     *
     * @param text1 첫 번째 텍스트
     * @param text2 두 번째 텍스트
     * @return 0.0 ~ 1.0 사이의 유사도 값
     */
    public static double calculateWithBM25Cosine(String text1, String text2) {
        return KOREAN_BM25_COSINE_PIPELINE.calculateSimilarity(text1, text2);
    }


    // 파이프라인 생성 메서드들
    public static SimilarityPipeline createKoreanJaccardPipeline() {
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        return new SimilarityPipeline(
            preprocessor,
            null, // Jaccard는 특징 추출이 필요하지 않음
            new JaccardSimilarityCalculator()
        );
    }

    public static SimilarityPipeline createKoreanTfCosinePipeline() {
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        return new SimilarityPipeline(
            preprocessor,
            new TfExtractor(),
            new CosineSimilarityCalculator()
        );
    }

    public static SimilarityPipeline createKoreanTfIdfCosinePipeline() {
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        return new SimilarityPipeline(
            preprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );
    }

    public static SimilarityPipeline createKoreanBM25CosinePipeline() {
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        return new SimilarityPipeline(
            preprocessor,
            new BM25Extractor(),
            new CosineSimilarityCalculator()
        );
    }
}