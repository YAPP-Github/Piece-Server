package org.yapp.core.similarity.preprocessing.cleaner;

/**
 * 텍스트 정제 인터페이스 입력된 텍스트에서 불필요한 문자를 제거하고 정규화하는 역할
 */
public interface TextCleaner {

    /**
     * 텍스트를 정제합니다.
     *
     * @param text 정제할 원본 텍스트
     * @return 정제된 텍스트
     */
    String clean(String text);

    /**
     * 정제기의 타입을 반환합니다.
     *
     * @return 정제기 타입
     */
    String getType();
}