package org.yapp.report.presentation.response;

import java.time.LocalDate;

public record ReportDetailResponse(Integer cnt, String reason, String reporterNickName,
                                   LocalDate reportedDate) {

}
