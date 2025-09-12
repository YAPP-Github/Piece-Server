package org.yapp.core.similarity.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.calculator.CosineSimilarityCalculator;
import org.yapp.core.similarity.calculator.JaccardSimilarityCalculator;
import org.yapp.core.similarity.feature.BM25Extractor;
import org.yapp.core.similarity.feature.TfIdfExtractor;
import org.yapp.core.similarity.preprocessing.TextPreprocessor;
import org.yapp.core.similarity.preprocessing.cleaner.KoreanTextCleaner;
import org.yapp.core.similarity.preprocessing.remover.KoreanStopWordRemover;
import org.yapp.core.similarity.preprocessing.tokenizer.KomoranTokenizer;
import org.yapp.core.similarity.preprocessing.tokenizer.WhitespaceTokenizer;

@Slf4j
class SimilarityPipelineTest {

    @Test
    @DisplayName("자카드에서 어절 단위 분석보다는 형태소 단위 분석이 유리함을 검증한다.")
    void jaccardPipeline_versus() {
        // given
        TextPreprocessor whiteSpacePreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline whiteSpacePipeline = new SimilarityPipeline(
            whiteSpacePreprocessor,
            null, // Jaccard는 특징 추출 불필요
            new JaccardSimilarityCalculator()
        );

        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline komoranPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            null, // Jaccard는 특징 추출 불필요
            new JaccardSimilarityCalculator()
        );

        String text1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String text2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        double whiteSpaceSimilarity = whiteSpacePipeline.calculateSimilarity(text1, text2);
        double komoranSimilarity = komoranPipeline.calculateSimilarity(text1, text2);

        // then
        log.info("Text 1: [{}]", text1);
        log.info("Text 2: [{}]", text2);
        log.info("Jaccard whiteSpace similarity: {}", whiteSpaceSimilarity);
        log.info("Jaccard komoran similarity: {}", komoranSimilarity);

        assertThat(whiteSpaceSimilarity).isBetween(0.0, 1.0);
        assertThat(komoranSimilarity).isBetween(0.0, 1.0);
        assertThat(komoranSimilarity).isGreaterThan(whiteSpaceSimilarity);
    }

    @Test
    @DisplayName("TF-IDF + 코사인 기반 파이프라인이 정상 동작한다")
    void tfIdfCosinePipeline_worksCorrectly() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline pipeline = new SimilarityPipeline(
            preprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );

        String text1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String text2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        double similarity = pipeline.calculateSimilarity(text1, text2);

        // then
        log.info("Text 1: [{}]", text1);
        log.info("Text 2: [{}]", text2);
        log.info("TF-IDF + Cosine similarity: {}", similarity);
        log.info("Pipeline info: {}", pipeline.getPipelineInfo());

        assertThat(similarity).isBetween(0.0, 1.0);
        assertThat(similarity).isPositive();
        assertThat(pipeline.getPipelineInfo()).contains("COSINE", "TF_IDF");
    }

    @Test
    @DisplayName("TF-IDF에서 어절 단위보다 형태소 단위 분석이 유리함을 검증한다")
    void tfIdfPipeline_versus() {
        // given
        // 1. 띄어쓰기(어절) 기반 파이프라인 설정
        TextPreprocessor whiteSpacePreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline whiteSpacePipeline = new SimilarityPipeline(
            whiteSpacePreprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );

        // 2. Komoran(형태소) 기반 파이프라인 설정
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline komoranPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );

        String text1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String text2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        SimilarityResult whiteSpaceSimilarityRet = whiteSpacePipeline.calculateSimilarityWithDetails(
            text1, text2);
        SimilarityResult komoranSimilarityRet = komoranPipeline.calculateSimilarityWithDetails(
            text1,
            text2);

        Double whiteSpaceSimilarity = whiteSpaceSimilarityRet.getSimilarity();
        Double komoranSimilarity = komoranSimilarityRet.getSimilarity();

        // then
        log.info("Text 1: [{}]", text1);
        log.info("Text 2: [{}]", text2);
        log.info("TF-IDF + Cosine (Whitespace) similarity: {}",
            whiteSpaceSimilarityRet.getSimilarity());
        log.info("TF-IDF + Cosine (Komoran) similarity: {}", komoranSimilarityRet.getSimilarity());

        assertThat(komoranSimilarity).isGreaterThan(whiteSpaceSimilarity);
    }

    @Test
    @DisplayName("Komoran+Jaccard: 동일 주제는 높은 점수, 다른 주제는 낮은 점수를 반환한다")
    void jaccard_handlesSameAndDifferentTopicsCorrectly() {
        // given
        // Komoran(형태소) 기반 자카드 파이프라인 설정
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline komoranJaccardPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            null, // Jaccard는 특징 추출 불필요
            new JaccardSimilarityCalculator()
        );

        // 100자 내외의 테스트 문장들
        String sameTopicText1 = "새로운 장소를 탐험하고 여러 문화를 경험하는 여행은 제게 큰 영감을 줍니다. 낯선 거리에서 느끼는 설렘과 현지 음식을 맛보는 즐거움은 삶의 활력소가 됩니다.";
        String sameTopicText2 = "저는 여행이 주는 특별한 경험을 사랑합니다. 새로운 사람들을 만나고 그들의 이야기를 듣는 것은 세상을 보는 시야를 넓혀주죠. 그래서 항상 다음 목적지를 꿈꿉니다.";
        String differentTopicText = "최근에는 효율적인 데이터 처리를 위한 백엔드 시스템 설계에 집중하고 있습니다. 복잡한 문제를 해결하고 코드 리뷰를 통해 동료와 함께 성장하는 과정이 즐겁습니다.";

        // when
        // 1. 동일 주제 유사도 계산
        double sameTopicSimilarity = komoranJaccardPipeline.calculateSimilarity(sameTopicText1,
            sameTopicText2);
        // 2. 다른 주제 유사도 계산
        double differentTopicSimilarity = komoranJaccardPipeline.calculateSimilarity(sameTopicText1,
            differentTopicText);

        // then
        log.info("[Jaccard] 동일 주제 유사도: {}", sameTopicSimilarity);
        log.info("[Jaccard] 다른 주제 유사도: {}", differentTopicSimilarity);

        // 최종적으로, 동일 주제 유사도가 다른 주제 유사도보다 훨씬 높아야 함
        assertThat(sameTopicSimilarity).isGreaterThan(differentTopicSimilarity);
    }

    @Test
    @DisplayName("Komoran+TF-IDF+Cosine: 동일 주제는 높은 점수, 다른 주제는 낮은 점수를 반환한다")
    void tfIdfCosine_handlesSameAndDifferentTopicsCorrectly() {
        // given
        // Komoran(형태소) 기반 TF-IDF + 코사인 파이프라인 설정
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline komoranTfIdfPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new TfIdfExtractor(),
            new CosineSimilarityCalculator()
        );

        // 100자 내외의 테스트 문장들
        String sameTopicText1 = "새로운 장소를 탐험하고 여러 문화를 경험하는 여행은 제게 큰 영감을 줍니다. 낯선 거리에서 느끼는 설렘과 현지 음식을 맛보는 즐거움은 삶의 활력소가 됩니다.";
        String sameTopicText2 = "저는 여행이 주는 특별한 경험을 사랑합니다. 새로운 사람들을 만나고 그들의 이야기를 듣는 것은 세상을 보는 시야를 넓혀주죠. 그래서 항상 다음 목적지를 꿈꿉니다.";
        String differentTopicText = "최근에는 효율적인 데이터 처리를 위한 백엔드 시스템 설계에 집중하고 있습니다. 복잡한 문제를 해결하고 코드 리뷰를 통해 동료와 함께 성장하는 과정이 즐겁습니다.";

        // when
        // 1. 동일 주제 유사도 계산
        double sameTopicSimilarity = komoranTfIdfPipeline.calculateSimilarity(sameTopicText1,
            sameTopicText2);
        // 2. 다른 주제 유사도 계산
        double differentTopicSimilarity = komoranTfIdfPipeline.calculateSimilarity(sameTopicText1,
            differentTopicText);

        // then
        log.info("[TF-IDF] 동일 주제 유사도: {}", sameTopicSimilarity);
        log.info("[TF-IDF] 다른 주제 유사도: {}", differentTopicSimilarity);

        // 최종적으로, 동일 주제 유사도가 다른 주제 유사도보다 훨씬 높아야 함
        assertThat(sameTopicSimilarity).isGreaterThan(differentTopicSimilarity);
    }

    @Test
    @DisplayName("파이프라인이 상세 결과를 올바르게 제공한다")
    void pipeline_providesDetailedResults() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline pipeline = new SimilarityPipeline(
            preprocessor,
            null,
            new JaccardSimilarityCalculator()
        );

        String text1 = "저는 여행을 좋아해요!";
        String text2 = "저는 사진 찍는 것을 좋아해요.";

        // when
        SimilarityResult result = pipeline.calculateSimilarityWithDetails(text1, text2);

        // then
        log.info("Detailed result:\n{}", result.getDetailedAnalysis());

        assertThat(result.getSimilarity()).isBetween(0.0, 1.0);
        assertThat(result.getProcessedTokens1()).contains("좋아해요");
        assertThat(result.getProcessedTokens2()).contains("좋아해요");
        assertThat(result.getProcessingTimeNanos()).isPositive();
        assertThat(result.getPipelineInfo()).isNotEmpty();
    }

    @Test
    @DisplayName("파이프라인이 null과 빈 문자열을 올바르게 처리한다")
    void pipeline_handlesNullAndEmptyStrings() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline pipeline = new SimilarityPipeline(
            preprocessor,
            null,
            new JaccardSimilarityCalculator()
        );

        // when & then
        assertThat(pipeline.calculateSimilarity(null, null)).isEqualTo(1.0);
        assertThat(pipeline.calculateSimilarity(null, "테스트")).isEqualTo(0.0);
        assertThat(pipeline.calculateSimilarity("테스트", null)).isEqualTo(0.0);
        assertThat(pipeline.calculateSimilarity("", "")).isEqualTo(1.0);
        assertThat(pipeline.calculateSimilarity("", "테스트")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("파이프라인 생성 시 필수 파라미터 검증이 동작한다")
    void pipeline_validatesRequiredParameters() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        // when & then
        // 전처리기가 null인 경우
        assertThatThrownBy(
            () -> new SimilarityPipeline(null, null, new JaccardSimilarityCalculator()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("TextPreprocessor cannot be null");

        // 유사도 계산기가 null인 경우
        assertThatThrownBy(() -> new SimilarityPipeline(preprocessor, null, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("SimilarityCalculator cannot be null");

        // 특징 벡터가 필요한 계산기에 특징 추출기가 null인 경우
        assertThatThrownBy(
            () -> new SimilarityPipeline(preprocessor, null, new CosineSimilarityCalculator()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(
                "FeatureExtractor cannot be null when SimilarityCalculator requires feature vectors");
    }

    @Test
    @DisplayName("파이프라인 성능이 합리적인 범위에 있다")
    void pipeline_performsWithinReasonableTime() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline pipeline = new SimilarityPipeline(
            preprocessor, null, new JaccardSimilarityCalculator());

        String longText1 = "저는 세계 곳곳을 여행하며 다양한 문화와 풍경의 아름다운 사진을 찍는 것을 사랑합니다. 사진 촬영은 제 열정이며 여행은 제게 영감을 줍니다.";
        String longText2 = "여행 사진은 새로운 장소를 탐험하는 것에 대한 제 사랑과, 카메라 렌즈를 통해 아름다운 순간과 추억을 포착하는 것을 결합하기 때문에 정말 멋집니다.";

        // when
        long startTime = System.nanoTime();
        SimilarityResult result = pipeline.calculateSimilarityWithDetails(longText1, longText2);
        long endTime = System.nanoTime();

        // then
        double actualProcessingTime = (endTime - startTime) / 1_000_000.0; // ms
        double reportedProcessingTime = result.getProcessingTimeMillis();

        log.info("Actual processing time: {} ms", actualProcessingTime);
        log.info("Reported processing time: {} ms", reportedProcessingTime);
        log.info("Similarity: {}", result.getSimilarity());

        // 처리 시간이 합리적인 범위에 있어야 함 (100ms 이하)
        assertThat(actualProcessingTime).isLessThan(100.0);
        assertThat(reportedProcessingTime).isLessThan(100.0);
        assertThat(result.getSimilarity()).isBetween(0.0, 1.0);
    }

    @Test
    @DisplayName("Komoran+BM25+Cosine: 동일 주제는 높은 점수, 다른 주제는 낮은 점수를 반환한다")
    void bm25Cosine_handlesSameAndDifferentTopicsCorrectly() {
        // given
        // Komoran(형태소) 기반 BM25 + 코사인 파이프라인 설정
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline komoranBM25Pipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new BM25Extractor(),
            new CosineSimilarityCalculator()
        );

        // 100자 내외의 테스트 문장들
        String sameTopicText1 = "새로운 장소를 탐험하고 여러 문화를 경험하는 여행은 제게 큰 영감을 줍니다. 낯선 거리에서 느끼는 설렘과 현지 음식을 맛보는 즐거움은 삶의 활력소가 됩니다.";
        String sameTopicText2 = "저는 여행이 주는 특별한 경험을 사랑합니다. 새로운 사람들을 만나고 그들의 이야기를 듣는 것은 세상을 보는 시야를 넓혀주죠. 그래서 항상 다음 목적지를 꿈꿉니다.";
        String differentTopicText = "최근에는 효율적인 데이터 처리를 위한 백엔드 시스템 설계에 집중하고 있습니다. 복잡한 문제를 해결하고 코드 리뷰를 통해 동료와 함께 성장하는 과정이 즐겁습니다.";

        // when
        // 1. 동일 주제 유사도 계산
        double sameTopicSimilarity = komoranBM25Pipeline.calculateSimilarity(sameTopicText1,
            sameTopicText2);
        // 2. 다른 주제 유사도 계산
        double differentTopicSimilarity = komoranBM25Pipeline.calculateSimilarity(sameTopicText1,
            differentTopicText);

        // then
        log.info("[BM25] 동일 주제 유사도: {}", sameTopicSimilarity);
        log.info("[BM25] 다른 주제 유사도: {}", differentTopicSimilarity);

        // 최종적으로, 동일 주제 유사도가 다른 주제 유사도보다 훨씬 높아야 함
        assertThat(sameTopicSimilarity).isGreaterThan(differentTopicSimilarity);
    }

    @Test
    @DisplayName("BM25 + 코사인 기반 파이프라인이 정상 동작한다")
    void bm25CosinePipeline_worksCorrectly() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );

        SimilarityPipeline pipeline = new SimilarityPipeline(
            preprocessor,
            new BM25Extractor(),
            new CosineSimilarityCalculator()
        );

        String text1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String text2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        double similarity = pipeline.calculateSimilarity(text1, text2);

        // then
        log.info("Text 1: [{}]", text1);
        log.info("Text 2: [{}]", text2);
        log.info("BM25 + Cosine similarity: {}", similarity);
        log.info("Pipeline info: {}", pipeline.getPipelineInfo());

        assertThat(similarity).isBetween(0.0, 1.0);
        assertThat(similarity).isPositive();
        assertThat(pipeline.getPipelineInfo()).contains("COSINE", "BM25");
    }

    @Test
    @DisplayName("BM25에서 어절 단위보다 형태소 단위 분석이 유리함을 검증한다")
    void bm25Pipeline_versus() {
        // given
        // 1. 띄어쓰기(어절) 기반 파이프라인 설정
        TextPreprocessor whiteSpacePreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline whiteSpacePipeline = new SimilarityPipeline(
            whiteSpacePreprocessor,
            new BM25Extractor(),
            new CosineSimilarityCalculator()
        );

        // 2. Komoran(형태소) 기반 파이프라인 설정
        TextPreprocessor komoranPreprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new KomoranTokenizer(),
            new KoreanStopWordRemover()
        );
        SimilarityPipeline komoranPipeline = new SimilarityPipeline(
            komoranPreprocessor,
            new BM25Extractor(),
            new CosineSimilarityCalculator()
        );

        String text1 = "저는 여행하고 사진 촬영하는 것을 좋아해요";
        String text2 = "저는 여행과 사진 촬영을 좋아해요";

        // when
        SimilarityResult whiteSpaceSimilarityRet = whiteSpacePipeline.calculateSimilarityWithDetails(
            text1, text2);
        SimilarityResult komoranSimilarityRet = komoranPipeline.calculateSimilarityWithDetails(
            text1,
            text2);

        Double whiteSpaceSimilarity = whiteSpaceSimilarityRet.getSimilarity();
        Double komoranSimilarity = komoranSimilarityRet.getSimilarity();

        // then
        log.info("Text 1: [{}]", text1);
        log.info("Text 2: [{}]", text2);
        log.info("BM25 + Cosine (Whitespace) similarity: {}",
            whiteSpaceSimilarityRet.getSimilarity());
        log.info("BM25 + Cosine (Komoran) similarity: {}", komoranSimilarityRet.getSimilarity());

        assertThat(komoranSimilarity).isGreaterThan(whiteSpaceSimilarity);
    }
}