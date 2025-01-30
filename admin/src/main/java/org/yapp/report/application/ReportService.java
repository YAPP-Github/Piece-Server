package org.yapp.report.application;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yapp.domain.report.Report;
import org.yapp.format.PageResponse;
import org.yapp.report.application.dto.ReportedUserWithReasonDto;
import org.yapp.report.dao.ReportRepository;
import org.yapp.report.presentation.response.ReportDetailResponse;
import org.yapp.report.presentation.response.ReportUserResponse;

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

    public PageResponse<ReportDetailResponse> getReportsByReportedUserId(Long reportedUserId,
        int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        int order = 1;
        Page<Report> reportPage = reportRepository.findAllByReportedUserIdOrderByCreatedAt(
            reportedUserId, pageable);
        List<ReportDetailResponse> content = new ArrayList<>();

        for (Report report : reportPage.getContent()) {
            content.add(new ReportDetailResponse(
                order++,
                report.getReason(),
                report.getCreatedAt().toLocalDate()
            ));
        }

        return new PageResponse<>(
            content,
            reportPage.getNumber(),
            reportPage.getSize(),
            reportPage.getTotalPages(),
            reportPage.getTotalElements(),
            reportPage.isFirst(),
            reportPage.isLast()
        );
    }
}
