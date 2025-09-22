package org.yapp.global.util;

import java.util.Base64;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneNumberDecoder {

    // 한국 핸드폰 번호 패턴: 010-XXXX-XXXX, 010XXXXXXXX 등
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
        "^(01[0-9]-?\\d{3,4}-?\\d{4}|01[0-9]\\d{7,8})$"
    );

    // Base64 문자열 패턴: A-Z, a-z, 0-9, +, /, = 만 포함
    private static final Pattern BASE64_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+/]*={0,2}$"
    );

    /**
     * 문자열이 Base64로 인코딩된 핸드폰 번호인지 판단하고 디코딩합니다.
     *
     * @param phoneNumber 인코딩되었을 수 있는 핸드폰 번호
     * @return 디코딩된 핸드폰 번호
     */
    public static String decodeIfBase64(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return phoneNumber;
        }

        // 이미 핸드폰 번호 형태라면 그대로 반환
        if (isValidPhoneNumber(phoneNumber)) {
            log.debug("Raw phone number detected: {}", maskPhoneNumber(phoneNumber));
            return phoneNumber;
        }

        // Base64로 인코딩된 것으로 보이면 디코딩 시도
        if (isLikelyBase64(phoneNumber)) {
            try {
                String decoded = decodeBase64(phoneNumber);
                if (isValidPhoneNumber(decoded)) {
                    log.debug("Base64 decoded phone number: {} -> {}", phoneNumber,
                        maskPhoneNumber(decoded));
                    return decoded;
                } else {
                    log.warn("Base64 decoded but not valid phone number: {}", phoneNumber);
                }
            } catch (Exception e) {
                log.warn("Failed to decode Base64 string: {}, error: {}", phoneNumber,
                    e.getMessage());
            }
        }

        // 디코딩에 실패하거나 유효하지 않으면 원본 반환
        log.debug("Returning original phone number: {}", phoneNumber);
        return phoneNumber;
    }

    /**
     * 문자열이 유효한 핸드폰 번호인지 확인
     */
    private static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * 문자열이 Base64로 인코딩되었을 가능성이 있는지 확인 - Base64 문자셋만 포함 - 길이가 4의 배수 - 핸드폰 번호 패턴과 다름
     */
    private static boolean isLikelyBase64(String str) {
        if (str == null || str.length() < 4) {
            return false;
        }

        // Base64는 길이가 4의 배수여야 함
        if (str.length() % 4 != 0) {
            return false;
        }

        // Base64 문자셋만 포함하는지 확인
        return BASE64_PATTERN.matcher(str).matches();
    }

    /**
     * Base64 문자열을 디코딩
     */
    private static String decodeBase64(String base64String) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            return new String(decodedBytes, "UTF-8");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Base64 string: " + base64String, e);
        }
    }

    /**
     * 로깅용 핸드폰 번호 마스킹 (개인정보 보호)
     */
    private static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return "****";
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(
            Math.max(3, phoneNumber.length() - 4));
    }
}