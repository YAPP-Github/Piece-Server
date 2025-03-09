package org.yapp.report.presentation.response;

import java.time.LocalDate;

public record ReportUserResponse(Long userId, String nickName, String name, LocalDate birthdate,
                                 Long totalReportedCnt, String latestReportedReason) {

}
