package org.yapp.core.similarity.preprocessing.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * 공백 기반 토크나이저 공백과 특수문자를 기준으로 텍스트를 토큰화합니다.
 */
public class WhitespaceTokenizer implements SentenceTokenizer {

    @Override
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tokens = new ArrayList<>();

        // 한글, 영어, 숫자만 유지하고 나머지는 공백으로 치환
        String cleanText = text.replaceAll("[^가-힣a-zA-Z0-9\\s]", " ");

        // 연속된 공백을 하나로 합치고 분리
        String[] words = cleanText.trim().replaceAll("\\s+", " ").split("\\s");

        for (String word : words) {
            String token = word.toLowerCase().trim();
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }

        return tokens;
    }

    @Override
    public String getType() {
        return "WHITESPACE";
    }
}