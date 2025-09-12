package org.yapp.core.similarity.preprocessing.cleaner;

import java.util.regex.Pattern;

/**
 * 한국어 특화 텍스트 정제기 한국어 텍스트의 특성을 고려한 정제를 수행
 */
public class KoreanTextCleaner implements TextCleaner {

    // 한국어 문자, 영문자, 숫자, 공백만 유지
    private static final Pattern KOREAN_CHARS = Pattern.compile("[^\\p{IsHangul}\\p{L}\\p{N}\\s]");
    private static final Pattern MULTIPLE_SPACES = Pattern.compile("\\s+");

    @Override
    public String clean(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // 한국어, 영문, 숫자, 공백만 유지
        String cleaned = KOREAN_CHARS.matcher(text).replaceAll(" ");

        // 연속된 공백을 단일 공백으로 변환
        cleaned = MULTIPLE_SPACES.matcher(cleaned).replaceAll(" ");

        // 앞뒤 공백 제거
        return cleaned.trim();
    }

    @Override
    public String getType() {
        return "KOREAN";
    }
}