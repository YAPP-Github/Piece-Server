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
import org.yapp.domain.value.ValueTalk;
import org.yapp.domain.value.application.ValueTalkService;
import org.yapp.domain.value.presentation.dto.response.ValueTalkResponses;
import org.yapp.util.CommonResponse;

@RequestMapping("/api/valueTalks")
@RestController
@RequiredArgsConstructor
public class ValueTalkController {

    private final ValueTalkService valueTalkService;

    @GetMapping()
    @Operation(summary = "가치관 톡 리스트 조회", description = "활성화된 가치관 톡 질문을 조회합니다.", tags = {
        "가치관 톡"})
    @ApiResponse(responseCode = "200", description = "가치관 톡 리스트가 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ValueTalkResponses>> getValueTalks() {
        List<ValueTalk> valueTalks = valueTalkService.getAllActiveValueTalks();
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(ValueTalkResponses.from(valueTalks)));
    }

}
