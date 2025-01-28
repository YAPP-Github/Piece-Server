package org.yapp.report.application.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.yapp.domain.user.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ReportedUserWithReasonDto {

    private User user;
    private Long reportCount;
    private String latestReason;
}