package org.yapp.core.similarity.preprocessing.remover;

import java.util.List;

/**
 * 불용어 제거 인터페이스 토큰 리스트에서 불용어를 제거하는 역할
 */
public interface StopWordRemover {

    /**
     * 토큰 리스트에서 불용어를 제거합니다.
     *
     * @param tokens 불용어 제거 대상 토큰 리스트
     * @return 불용어가 제거된 토큰 리스트
     */
    List<String> removeStopWords(List<String> tokens);

    /**
     * 불용어 제거기의 타입을 반환합니다.
     *
     * @return 불용어 제거기 타입
     */
    String getType();
}