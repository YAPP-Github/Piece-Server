package org.yapp.match.presentation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.format.CommonResponse;
import org.yapp.match.application.command.ManualMatchReservationCommandService;
import org.yapp.match.application.query.ManualMatchHistoryQueryService;
import org.yapp.match.application.query.ManualMatchUserQueryService;
import org.yapp.match.dto.request.ManualMatchCancelRequest;
import org.yapp.match.dto.request.ManualMatchReservationRequest;
import org.yapp.match.dto.response.ManualMatchCandidateListResponse;
import org.yapp.match.dto.response.ManualMatchHistoryResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/v1/manual-match")
public class ManualMatchController {

    private final ManualMatchHistoryQueryService manualMatchHistoryQueryService;
    private final ManualMatchUserQueryService manualMatchUserQueryService;
    private final ManualMatchReservationCommandService manualMatchReservationCommandService;

    @GetMapping("/candidates")
    public ResponseEntity<CommonResponse<ManualMatchCandidateListResponse>> getManualMatchingCandidateList(
        @RequestParam(defaultValue = "0", name = "page") int page) {
        ManualMatchCandidateListResponse candidateList =
            manualMatchUserQueryService.getCandidateList(page);

        return ResponseEntity.ok(CommonResponse.createSuccess(candidateList));
    }

    @GetMapping("/history")
    public ResponseEntity<CommonResponse<List<ManualMatchHistoryResponse>>> getManualMatchingHistory(
        @RequestParam(defaultValue = "0", name = "page") int page) {
        List<ManualMatchHistoryResponse> manualMatchHistory =
            manualMatchHistoryQueryService.getManualMatchHistory(page);

        return ResponseEntity.ok(CommonResponse.createSuccess(manualMatchHistory));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> createManualMatchHistory(
        @RequestBody ManualMatchReservationRequest request
    ) {
        manualMatchReservationCommandService.reserveManualMatching(request);
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteManualMatchHistory(
        @RequestBody ManualMatchCancelRequest request
    ) {
        manualMatchReservationCommandService.cancelManualMatching(request);
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }
}
