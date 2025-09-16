package org.yapp.core.similarity.preprocessing.remover;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 한국어 불용어 제거기 한국어의 대표적인 불용어들을 제거
 */
public class KoreanStopWordRemover implements StopWordRemover {

    private static final Set<String> KOREAN_STOP_WORDS = Set.of(
        "은", "는", "이", "가", "을", "를", "에", "에서", "로", "으로",
        "와", "과", "도", "만", "의", "에게", "한테", "께", "부터", "까지",
        "하다", "있다", "없다", "되다", "같다", "다르다", "좋다", "나쁘다",
        "그", "저", "그것", "이것", "저것", "여기", "거기", "저기",
        "나", "너", "우리", "그들", "자신", "서로", "모두", "각각",
        "그런", "이런", "저런", "어떤", "무엇", "언제", "어디", "왜", "어떻게",
        "매우", "정말", "참", "너무", "아주", "조금", "많이", "적게",
        "그리고", "또", "그래서", "하지만", "그러나", "그런데", "따라서",
        "만약", "때문", "위해", "통해", "대해", "관해", "대한", "에 대한"
    );

    @Override
    public List<String> removeStopWords(List<String> tokens) {
        if (tokens == null) {
            return List.of();
        }

        return tokens.stream()
            .filter(token -> !KOREAN_STOP_WORDS.contains(token))
            .collect(Collectors.toList());
    }

    @Override
    public String getType() {
        return "KOREAN";
    }
}