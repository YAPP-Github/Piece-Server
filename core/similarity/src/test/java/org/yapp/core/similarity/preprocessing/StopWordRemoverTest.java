package org.yapp.core.similarity.preprocessing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.core.similarity.preprocessing.remover.KoreanStopWordRemover;

@Slf4j
class StopWordRemoverTest {

    @Test
    @DisplayName("한국어 불용어 제거기가 불용어를 제거한다")
    void koreanStopWordRemover_removesStopWords() {
        // given
        KoreanStopWordRemover remover = new KoreanStopWordRemover();
        List<String> tokens = List.of("나", "는", "여행", "을", "좋아", "하다", "정말");

        // when
        List<String> result = remover.removeStopWords(tokens);

        // then
        log.info("Original tokens: {}", tokens);
        log.info("After stop word removal: {}", result);

        assertThat(result)
            .contains("여행", "좋아")
            .doesNotContain("나", "는", "을", "하다");
    }

    @Test
    @DisplayName("불용어 제거기가 null과 빈 리스트를 처리한다")
    void stopWordRemover_handlesNullAndEmpty() {
        // given
        KoreanStopWordRemover koreanRemover = new KoreanStopWordRemover();

        // when & then
        assertThat(koreanRemover.removeStopWords(null)).isEmpty();
        assertThat(koreanRemover.removeStopWords(List.of())).isEmpty();
    }

    @Test
    @DisplayName("불용어 제거기가 타입을 올바르게 반환한다")
    void stopWordRemover_returnsCorrectType() {
        // given
        KoreanStopWordRemover koreanRemover = new KoreanStopWordRemover();

        // when & then
        assertThat(koreanRemover.getType()).isEqualTo("KOREAN");
    }
}