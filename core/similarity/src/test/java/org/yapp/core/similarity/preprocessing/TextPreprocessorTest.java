package org.yapp.core.similarity.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.preprocessing.cleaner.KoreanTextCleaner;
import org.yapp.core.similarity.preprocessing.remover.KoreanStopWordRemover;
import org.yapp.core.similarity.preprocessing.tokenizer.WhitespaceTokenizer;

@Slf4j
class TextPreprocessorTest {

    @Test
    @DisplayName("한국어 전처리 파이프라인이 정상 동작한다")
    void koreanPreprocessor_worksCorrectly() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );
        String input = "안녕하세요! 저는 여행을 정말 좋아해요.";

        // when
        List<String> result = preprocessor.preprocess(input);

        // then
        log.info("Input: [{}]", input);
        log.info("Preprocessed: {}", result);
        log.info("Pipeline info: {}", preprocessor.getPreprocessorInfo());

        assertThat(result)
            .contains("안녕하세요", "여행을", "좋아해요")
            .doesNotContain("정말"); // 불용어 제거됨
    }

    @Test
    @DisplayName("전처리기가 null과 빈 문자열을 처리한다")
    void preprocessor_handlesNullAndEmpty() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        // when & then
        assertThat(preprocessor.preprocess(null)).isEmpty();
        assertThat(preprocessor.preprocess("")).isEmpty();
        assertThat(preprocessor.preprocess("   ")).isEmpty();
    }

    @Test
    @DisplayName("전처리기가 파이프라인 정보를 올바르게 제공한다")
    void preprocessor_providesCorrectPipelineInfo() {
        // given
        TextPreprocessor preprocessor = new TextPreprocessor(
            new KoreanTextCleaner(),
            new WhitespaceTokenizer(),
            new KoreanStopWordRemover()
        );

        // when
        String info = preprocessor.getPreprocessorInfo();

        // then
        log.info("Pipeline info: {}", info);

        assertThat(info)
            .contains("TextPreprocessor")
            .contains("KOREAN") // Cleaner type
            .contains("WHITESPACE") // Tokenizer type
            .contains("KOREAN"); // StopWordRemover type
    }
}