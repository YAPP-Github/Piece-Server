package org.yapp.unit.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yapp.global.util.PhoneNumberDecoder;

@Slf4j
class PhoneNumberDecoderTest {

    @Test
    @DisplayName("유효한 핸드폰 번호는 그대로 반환")
    void shouldReturnRawPhoneNumberWhenValid() {
        // Given
        String validPhoneNumber = "01012345678";

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(validPhoneNumber);

        // Then
        assertThat(result).isEqualTo(validPhoneNumber);
    }

    @Test
    @DisplayName("하이픈이 포함된 핸드폰 번호도 그대로 반환")
    void shouldReturnRawPhoneNumberWithHyphen() {
        // Given
        String phoneNumberWithHyphen = "010-1234-5678";

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(phoneNumberWithHyphen);

        // Then
        assertThat(result).isEqualTo(phoneNumberWithHyphen);
    }

    @Test
    @DisplayName("Base64로 인코딩된 핸드폰 번호는 디코딩하여 반환")
    void shouldDecodeBase64EncodedPhoneNumber() {
        // Given
        String originalPhoneNumber = "01012345678";
        String base64EncodedPhoneNumber = Base64.getEncoder()
            .encodeToString(originalPhoneNumber.getBytes());

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(base64EncodedPhoneNumber);

        // Then
        assertThat(result).isEqualTo(originalPhoneNumber);
    }

    @Test
    @DisplayName("Base64로 인코딩된 하이픈 포함 핸드폰 번호는 디코딩하여 반환")
    void shouldDecodeBase64EncodedPhoneNumberWithHyphen() {
        // Given
        String originalPhoneNumber = "010-1234-5678";
        String base64EncodedPhoneNumber = Base64.getEncoder()
            .encodeToString(originalPhoneNumber.getBytes());

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(base64EncodedPhoneNumber);

        // Then
        assertThat(result).isEqualTo(originalPhoneNumber);
    }

    @Test
    @DisplayName("Base64처럼 보이지만 유효하지 않은 핸드폰 번호가 나오면 원본 반환")
    void shouldReturnOriginalWhenBase64DecodedButNotValidPhoneNumber() {
        // Given
        String invalidPhoneNumber = "hello world";
        String base64String = Base64.getEncoder().encodeToString(invalidPhoneNumber.getBytes());

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(base64String);

        // Then
        assertThat(result).isEqualTo(base64String); // 원본 반환
    }

    @Test
    @DisplayName("잘못된 Base64 문자열인 경우 원본 반환")
    void shouldReturnOriginalWhenInvalidBase64() {
        // Given
        String invalidBase64 = "invalid-base64-string";

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(invalidBase64);

        // Then
        assertThat(result).isEqualTo(invalidBase64);
    }

    @Test
    @DisplayName("null이나 빈 문자열은 그대로 반환")
    void shouldReturnNullOrEmptyAsIs() {
        // When & Then
        assertThat(PhoneNumberDecoder.decodeIfBase64(null)).isNull();
        assertThat(PhoneNumberDecoder.decodeIfBase64("")).isEmpty();
    }

    @Test
    @DisplayName("길이가 4의 배수가 아닌 문자열은 Base64로 판단하지 않음")
    void shouldNotConsiderNonMultipleOf4LengthAsBase64() {
        // Given
        String shortString = "abc"; // 길이 3

        // When
        String result = PhoneNumberDecoder.decodeIfBase64(shortString);

        // Then
        assertThat(result).isEqualTo(shortString);
    }

    @Test
    @DisplayName("다양한 핸드폰 번호 형식 테스트")
    void shouldHandleVariousPhoneNumberFormats() {
        // Given
        String[] validFormats = {
            "01012345678",
            "010-1234-5678",
            "01087654321",
            "010-8765-4321"
        };

        // When & Then
        for (String phoneNumber : validFormats) {
            String result = PhoneNumberDecoder.decodeIfBase64(phoneNumber);
            assertThat(result).isEqualTo(phoneNumber);
        }
    }

    @Test
    @DisplayName("실제 iOS에서 보낼 수 있는 Base64 인코딩된 핸드폰 번호 시나리오")
    void shouldHandleRealIosScenario() {
        // Given
        String[] originalPhoneNumbers = {
            "01012345678",
            "010-1234-5678",
            "01087654321"
        };

        for (String originalPhoneNumber : originalPhoneNumbers) {
            // iOS가 Base64로 인코딩해서 보낸다고 가정
            String base64Encoded = Base64.getEncoder()
                .encodeToString(originalPhoneNumber.getBytes());

            // When
            String result = PhoneNumberDecoder.decodeIfBase64(base64Encoded);

            // Then
            assertThat(result).isEqualTo(originalPhoneNumber);
        }
    }
}