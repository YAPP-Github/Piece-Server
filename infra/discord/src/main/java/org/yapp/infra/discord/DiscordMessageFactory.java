package org.yapp.infra.discord;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.yapp.infra.discord.dto.DiscordEmbed;
import org.yapp.infra.discord.dto.DiscordMessage;
import org.yapp.infra.discord.dto.DiscordMessageType;

public final class DiscordMessageFactory {

    public static final int ERROR_COLOR = 0xFF0000; // 빨강: 에러 메시지
    public static final int SUCCESS_COLOR = 0x00FF00; // 초록: 성공 메시지
    public static final int INFO_COLOR = 0x3498DB; // 파랑: 일반 정보 메시지
    public static final int WARNING_COLOR = 0xF1C40F; // 노랑: 경고 메시지
    public static final int ALERT_COLOR = 0xE67E22; // 주황: 에러-경고 중간 단계

    private DiscordMessageFactory() {
    }

    /**
     * HTTP 요청 에러 발생 시 Discord 메시지를 생성합니다.
     *
     * @param request HttpServletRequest (요청 정보)
     * @param ex      발생한 예외
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createErrorMessage(HttpServletRequest request, Throwable ex) {
        String content = String.format("Error occurred during [%s %s]",
            request.getMethod(), request.getRequestURI());
        String description = String.format("Exception: %s\nMessage: %s",
            ex.getClass().getName(), ex.getMessage());

        String headersAsString = Collections.list(request.getHeaderNames())
            .stream()
            .collect(Collectors.toMap(header -> header, request::getHeader))
            .entrySet()
            .stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining("\n"));

        DiscordEmbed embed = DiscordEmbed.of(
            "HTTP Request Error",
            description,
            ERROR_COLOR,
            List.of(
                DiscordEmbed.Field.of("Method", request.getMethod(), false),
                DiscordEmbed.Field.of("Path",
                    request.getRequestURI() + (request.getQueryString() != null ? "?"
                        + request.getQueryString() : ""),
                    false),
                DiscordEmbed.Field.of("Client IP", request.getRemoteAddr(), false),
                DiscordEmbed.Field.of("User-Agent", request.getHeader("User-Agent"), false),
                DiscordEmbed.Field.of("Exception", ex.getClass().getSimpleName(), false),
                DiscordEmbed.Field.of("Headers", headersAsString, false)));
        return DiscordMessage.of(DiscordMessageType.ERROR_MESSAGE, content, List.of(embed));
    }

    /**
     * 새 회원(프로필) 등록 시 Discord 메시지를 생성합니다.
     *
     * @param profileId 등록된 프로필의 식별자
     * @param nickname  회원의 닉네임
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createNewProfileMessage(Long profileId, String nickname) {
        DiscordEmbed embed = DiscordEmbed.of(
            "프로필 등록",
            "새로운 프로필이 등록되었습니다.",
            SUCCESS_COLOR,
            List.of(
                DiscordEmbed.Field.of("프로필 식별 값", profileId.toString(), true),
                DiscordEmbed.Field.of("닉네임", nickname, true)));
        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, null, List.of(embed));
    }

    /**
     * 반려된 프로필이 갱신되어 재등록된 경우 Discord 메시지를 생성합니다.
     *
     * @param profileId 프로필 식별자
     * @param nickname  프로필 닉네임
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createRenewProfileMessage(Long profileId, String nickname) {
        DiscordEmbed embed = DiscordEmbed.of(
            "프로필 재심사",
            "반려된 프로필이 수정되어 다시 제출되었습니다.",
            WARNING_COLOR,
            List.of(
                DiscordEmbed.Field.of("프로필 식별 값", profileId.toString(), true),
                DiscordEmbed.Field.of("닉네임", nickname, true)));
        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, null, List.of(embed));
    }

    /**
     * 사용자가 프로필 이미지를 수정한 경우 Discord 메시지를 생성합니다.
     *
     * @param profileId 프로필 식별자
     * @param nickname  프로필 닉네임
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createProfileImageUpdateMessage(Long profileId, String nickname) {
        DiscordEmbed embed = DiscordEmbed.of(
            "프로필 이미지 심사",
            "프로필 이미지가 수정되었습니다.",
            WARNING_COLOR,
            List.of(
                DiscordEmbed.Field.of("프로필 식별 값", profileId.toString(), true),
                DiscordEmbed.Field.of("닉네임", nickname, true)));
        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, null, List.of(embed));
    }

    /**
     * 사용자가 프로필 가치관 톡을 수정한 경우 Discord 메시지를 생성합니다.
     *
     * @param profileId 프로필 식별자
     * @param nickname  프로필 닉네임
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createProfileValueTalkUpdateMessage(Long profileId,
        String nickname) {
        DiscordEmbed embed = DiscordEmbed.of(
            "프로필 가치관 톡 수정",
            "프로필 가치관 톡이 수정되었습니다.",
            INFO_COLOR,
            List.of(
                DiscordEmbed.Field.of("프로필 식별 값", profileId.toString(), true),
                DiscordEmbed.Field.of("닉네임", nickname, true)));
        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, null, List.of(embed));
    }

    /**
     * 프로필이 신고당한 경우 Discord 메시지를 생성합니다
     *
     * @param userId   유저 식별자 (신고 당한)
     * @param nickname 프로필 닉네임 (신고 당한)
     * @return DiscordMessage 객체
     */
    public static DiscordMessage createUserReportedMessage(Long userId,
        String nickname, String reason) {
        DiscordEmbed embed = DiscordEmbed.of(
            "유저 신고",
            "해당 유저가 신고되었습니다.",
            ALERT_COLOR,
            List.of(
                DiscordEmbed.Field.of("유저 식별 값", userId.toString(), true),
                DiscordEmbed.Field.of("닉네임", nickname, true),
                DiscordEmbed.Field.of("신고 사유", reason, true)));

        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, null, List.of(embed));
    }


    /**
     * 기본 서비스 메시지를 생성합니다.
     *
     * @param content     메시지 내용
     * @param title       메시지 제목
     * @param description 메시지 설명
     */
    public static DiscordMessage createBasicServiceMessage(String content, String title,
        String description) {
        DiscordEmbed embed = DiscordEmbed.of(
            title,
            description,
            INFO_COLOR,
            null);
        return DiscordMessage.of(DiscordMessageType.SERVICE_MESSAGE, content, List.of(embed));
    }
}