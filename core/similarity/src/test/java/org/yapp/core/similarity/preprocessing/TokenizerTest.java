package org.yapp.core.similarity.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.preprocessing.tokenizer.KomoranTokenizer;
import org.yapp.core.similarity.preprocessing.tokenizer.WhitespaceTokenizer;

@Slf4j
class TokenizerTest {

    @Test
    @DisplayName("공백 토큰화기가 공백으로 토큰을 분리한다")
    void whitespaceTokenizer_splitsOnWhitespace() {
        // given
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        String text = "여행을 좋아해요 정말로";

        // when
        List<String> tokens = tokenizer.tokenize(text);

        // then
        log.info("Input: [{}]", text);
        log.info("Tokens: {}", tokens);

        assertThat(tokens)
            .hasSize(3)
            .containsExactly("여행을", "좋아해요", "정말로");
    }

    @Test
    @DisplayName("공백 토큰화기가 연속 공백을 처리한다")
    void whitespaceTokenizer_handlesMultipleSpaces() {
        // given
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        String text = "여행을   좋아해요     정말로";

        // when
        List<String> tokens = tokenizer.tokenize(text);

        // then
        log.info("Input: [{}]", text);
        log.info("Tokens: {}", tokens);

        assertThat(tokens)
            .hasSize(3)
            .containsExactly("여행을", "좋아해요", "정말로");
    }

    @Test
    @DisplayName("토큰화기가 null과 빈 문자열을 처리한다")
    void tokenizer_handlesNullAndEmpty() {
        // given
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();

        // when & then
        assertThat(tokenizer.tokenize(null)).isEmpty();
        assertThat(tokenizer.tokenize("")).isEmpty();
        assertThat(tokenizer.tokenize("   ")).isEmpty();
    }

    @Test
    @DisplayName("토큰화기가 타입을 올바르게 반환한다")
    void tokenizer_returnsCorrectType() {
        // given
        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();

        // when & then
        assertThat(tokenizer.getType()).isEqualTo("WHITESPACE");
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 형태소 분석을 수행한다")
    void komoranTokenizer_performsMorphemeAnalysis() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();
        String text = "여행을 좋아해요 정말로";

        // when
        List<String> tokens = tokenizer.tokenize(text);

        // then
        log.info("Input: [{}]", text);
        log.info("Morpheme tokens: {}", tokens);

        assertThat(tokens)
            .isNotEmpty()
            .contains("여행", "좋아하", "정말로");
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 복잡한 문장을 분석한다")
    void komoranTokenizer_analyzesComplexSentence() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();
        String text = "사람들과 소통하는 것을 중요하게 생각합니다";

        // when
        List<String> tokens = tokenizer.tokenize(text);

        // then
        log.info("Input: [{}]", text);
        log.info("Complex sentence tokens: {}", tokens);

        assertThat(tokens)
            .isNotEmpty()
            .contains("사람", "소통", "것", "생각");
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 의미있는 형태소만 추출한다")
    void komoranTokenizer_extractsMeaningfulMorphemes() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();
        String text = "안녕하세요! 저는 개발자입니다.";

        // when
        List<String> tokens = tokenizer.tokenize(text);

        // then
        log.info("Input: [{}]", text);
        log.info("Meaningful morphemes: {}", tokens);

        assertThat(tokens)
            .isNotEmpty()
            .contains("개발자")
            .doesNotContain("!", ".", "는", "입니다");
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 null과 빈 문자열을 처리한다")
    void komoranTokenizer_handlesNullAndEmpty() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();

        // when & then
        assertThat(tokenizer.tokenize(null)).isEmpty();
        assertThat(tokenizer.tokenize("")).isEmpty();
        assertThat(tokenizer.tokenize("   ")).isEmpty();
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 올바른 타입을 반환한다")
    void komoranTokenizer_returnsCorrectType() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();

        // when & then
        assertThat(tokenizer.getType()).isEqualTo("KOMORAN");
    }

    @Test
    @DisplayName("KOMORAN 토크나이저가 분석 오류 시 빈 리스트를 반환한다")
    void komoranTokenizer_returnsEmptyListOnError() {
        // given
        KomoranTokenizer tokenizer = new KomoranTokenizer();
        String problematicText = ""; // 빈 문자열이나 특수한 경우

        // when
        List<String> tokens = tokenizer.tokenize(problematicText);

        // then
        assertThat(tokens).isNotNull().isEmpty();
    }
}