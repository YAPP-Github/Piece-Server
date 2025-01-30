package org.yapp.domain.term.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yapp.domain.term.Term;
import org.yapp.domain.term.applicatoin.TermService;
import org.yapp.domain.term.applicatoin.TermUseCase;
import org.yapp.domain.term.applicatoin.dto.SignupTermsDto;
import org.yapp.domain.term.presentation.dto.request.TermAgreementRequest;
import org.yapp.domain.term.presentation.dto.response.TermResponses;
import org.yapp.format.CommonResponse;

@RequestMapping("/api/terms")
@Controller
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;
    private final TermUseCase termUseCase;

    @GetMapping()
    @Operation(summary = "약관 리스트 조회", description = "서비스에 등록된 모든 약관을 조회합니다.", tags = {"약관"})
    @ApiResponse(responseCode = "200", description = "약관 리스트가 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<TermResponses>> getAllTerms() {
        List<Term> allActiveTerms = termService.getAllActiveTerms();
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(TermResponses.from(allActiveTerms)));
    }

    @PostMapping("/agree")
    @Operation(summary = "사용자 약관 동의", description = "사용자가 설정한 약관을 등록합니다", tags = {"약관"})
    @ApiResponse(responseCode = "200", description = "사용자가 설정한 약관이 등록되었습니다.")
    public ResponseEntity<CommonResponse<Void>> agreeTerm(@AuthenticationPrincipal Long userId,
        @RequestBody TermAgreementRequest request) {
        termUseCase.checkTermConstraints(new SignupTermsDto(userId, request.agreedTermsId()));
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccessWithNoContent());
    }
}
