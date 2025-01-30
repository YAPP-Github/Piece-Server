package org.yapp.domain.value.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.value.ValuePick;
import org.yapp.domain.value.application.ValuePickService;
import org.yapp.domain.value.presentation.dto.response.ValuePickResponses;
import org.yapp.format.CommonResponse;

@RequestMapping("/api/valuePicks")
@RestController
@RequiredArgsConstructor
public class ValuePickController {

    private final ValuePickService valuePickService;

    @GetMapping()
    @Operation(summary = "가치관 픽 질문 리스트 조회", description = "활성화된 가치관 픽 질문을 조회합니다.", tags = {
        "가치관 픽"})
    @ApiResponse(responseCode = "200", description = "가치관 리스트가 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ValuePickResponses>> getValuePicks() {
        List<ValuePick> allValuePicks = valuePickService.getAllActiveValuePicks();
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(ValuePickResponses.from(allValuePicks)));
    }
}
