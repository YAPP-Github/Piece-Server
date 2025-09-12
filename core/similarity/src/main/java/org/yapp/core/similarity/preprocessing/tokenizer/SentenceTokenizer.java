package org.yapp.core.similarity.preprocessing.tokenizer;

import java.util.List;

/**
 * 문장 토큰화 인터페이스 텍스트를 토큰 리스트로 변환하는 역할을 담당 (빈도 정보를 보존하기 위해 List 사용)
 */
public interface SentenceTokenizer {

    /**
     * 텍스트를 토큰들로 분해합니다.
     *
     * @param text 분석할 텍스트
     * @return 토큰들의 리스트 (빈도 정보 포함)
     */
    List<String> tokenize(String text);

    /**
     * 토크나이저의 타입을 반환합니다.
     *
     * @return 토크나이저 타입
     */
    String getType();
}