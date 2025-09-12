package org.yapp.core.similarity.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}