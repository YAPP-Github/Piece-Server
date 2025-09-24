package org.yapp.core.similarity.preprocessing;

import java.util.List;
import org.yapp.core.similarity.preprocessing.cleaner.TextCleaner;
import org.yapp.core.similarity.preprocessing.remover.StopWordRemover;
import org.yapp.core.similarity.preprocessing.tokenizer.SentenceTokenizer;

/**
 * 텍스트 전처리 파이프라인 정제 → 토큰화 → 불용어 제거 순서로 텍스트를 전처리
 */
public class TextPreprocessor {

    private final TextCleaner textCleaner;
    private final SentenceTokenizer tokenizer;
    private final StopWordRemover stopWordRemover;

    public TextPreprocessor(TextCleaner textCleaner,
        SentenceTokenizer tokenizer,
        StopWordRemover stopWordRemover) {
        this.textCleaner = textCleaner;
        this.tokenizer = tokenizer;
        this.stopWordRemover = stopWordRemover;
    }

    /**
     * 텍스트를 전처리하여 정제된 토큰 리스트를 반환합니다.
     *
     * @param text 전처리할 원본 텍스트
     * @return 전처리된 토큰 리스트
     */
    public List<String> preprocess(String text) {
        if (text == null || text.trim().isEmpty()) {
            return List.of();
        }

        // 1. 텍스트 정제
        String cleanedText = textCleaner.clean(text);

        // 2. 토큰화
        List<String> tokens = tokenizer.tokenize(cleanedText);

        // 3. 불용어 제거
        return stopWordRemover.removeStopWords(tokens);
    }

    /**
     * 전처리 파이프라인의 상세 정보를 반환합니다.
     *
     * @return 전처리 파이프라인 정보
     */
    public String getPreprocessorInfo() {
        return String.format("TextPreprocessor[Cleaner=%s, Tokenizer=%s, StopWordRemover=%s]",
            textCleaner.getType(),
            tokenizer.getType(),
            stopWordRemover.getType());
    }
}