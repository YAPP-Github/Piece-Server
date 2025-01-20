package org.yapp.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yapp.report.application.dto.ReportedUserWithReasonDto;
import org.yapp.report.dao.ReportRepository;
import org.yapp.report.presentation.response.ReportUserResponse;
import org.yapp.util.PageResponse;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public PageResponse<ReportUserResponse> getReportedUsersWithCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ReportedUserWithReasonDto> reportedUserPage = reportRepository.findReportedUsersWithLatestReason(
            pageable);

        Page<ReportUserResponse> reportUserResponsePage = reportedUserPage.map(dto ->
            new ReportUserResponse(
                dto.getUser().getId(),
                dto.getUser().getProfile().getProfileBasic().getNickname(),
                dto.getUser().getName(),
                dto.getUser().getProfile().getProfileBasic().getBirthdate(),
                dto.getReportCount(),
                dto.getLatestReason()
            )
        );

        return new PageResponse<>(
            reportUserResponsePage.getContent(),
            reportUserResponsePage.getNumber(),
            reportUserResponsePage.getSize(),
            reportUserResponsePage.getTotalPages(),
            reportUserResponsePage.getTotalElements(),
            reportUserResponsePage.isFirst(),
            reportUserResponsePage.isLast()
        );
    }
}
