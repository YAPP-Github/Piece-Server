package org.yapp.core.domain.report.event;

import lombok.Getter;

@Getter
public class ReportEvent {

    private final Long userId;
    private final String nickname;
    private final String reason;

    public ReportEvent(Long userId, String nickname, String reason) {
        this.userId = userId;
        this.nickname = nickname;
        this.reason = reason;
    }
}
