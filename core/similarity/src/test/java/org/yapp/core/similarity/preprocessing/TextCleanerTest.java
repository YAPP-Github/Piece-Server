package org.yapp.core.similarity.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.preprocessing.cleaner.KoreanTextCleaner;

@Slf4j
class TextCleanerTest {

    @Test
    @DisplayName("기본 텍스트 정제기가 특수문자를 제거한다")
    void KoreanTextCleaner_removesSpecialCharacters() {
        // given
        KoreanTextCleaner cleaner = new KoreanTextCleaner();
        String input = "안녕하세요! 여행을 좋아해요? (정말로)";

        // when
        String result = cleaner.clean(input);

        // then
        log.info("Input: [{}]", input);
        log.info("Cleaned: [{}]", result);

        assertThat(result)
            .doesNotContain("!", "?", "(", ")")
            .contains("안녕하세요", "여행을", "좋아해요", "정말로");
    }

    @Test
    @DisplayName("기본 텍스트 정제기가 연속 공백을 정규화한다")
    void KoreanTextCleaner_normalizesSpaces() {
        // given
        KoreanTextCleaner cleaner = new KoreanTextCleaner();
        String input = "여행을   좋아해요     정말로";

        // when
        String result = cleaner.clean(input);

        // then
        log.info("Input: [{}]", input);
        log.info("Normalized: [{}]", result);

        assertThat(result).isEqualTo("여행을 좋아해요 정말로");
    }

    @Test
    @DisplayName("한국어 텍스트 정제기가 한글과 영문을 유지한다")
    void koreanTextCleaner_preservesKoreanAndEnglish() {
        // given
        KoreanTextCleaner cleaner = new KoreanTextCleaner();
        String input = "안녕하세요! Hello 123 여행을 좋아해요~";

        // when
        String result = cleaner.clean(input);

        // then
        log.info("Input: [{}]", input);
        log.info("Korean cleaned: [{}]", result);

        assertThat(result)
            .contains("안녕하세요", "Hello", "123", "여행을", "좋아해요")
            .doesNotContain("!", "~");
    }

    @Test
    @DisplayName("텍스트 정제기들이 null과 빈 문자열을 처리한다")
    void textCleaners_handleNullAndEmpty() {
        // given
        KoreanTextCleaner defaultCleaner = new KoreanTextCleaner();
        KoreanTextCleaner koreanCleaner = new KoreanTextCleaner();

        // when & then
        assertThat(defaultCleaner.clean(null)).isEmpty();
        assertThat(defaultCleaner.clean("")).isEmpty();
        assertThat(defaultCleaner.clean("   ")).isEmpty();

        assertThat(koreanCleaner.clean(null)).isEmpty();
        assertThat(koreanCleaner.clean("")).isEmpty();
        assertThat(koreanCleaner.clean("   ")).isEmpty();
    }

    @Test
    @DisplayName("텍스트 정제기들이 타입을 올바르게 반환한다")
    void textCleaners_returnCorrectTypes() {
        // given
        KoreanTextCleaner defaultCleaner = new KoreanTextCleaner();
        KoreanTextCleaner koreanCleaner = new KoreanTextCleaner();

        // when & then
        assertThat(defaultCleaner.getType()).isEqualTo("DEFAULT");
        assertThat(koreanCleaner.getType()).isEqualTo("KOREAN");
    }
}