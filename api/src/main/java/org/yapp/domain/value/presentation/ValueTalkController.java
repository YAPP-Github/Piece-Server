package org.yapp.domain.value.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.value.ValueTalk;
import org.yapp.domain.value.application.ValueTalkService;
import org.yapp.domain.value.presentation.dto.ValueTalkResponses;
import org.yapp.util.CommonResponse;

import java.util.List;

@RequestMapping("/api/valueTalks")
@RestController
@RequiredArgsConstructor
public class ValueTalkController {
    private final ValueTalkService valueTalkService;

    @GetMapping()
    @Operation(summary = "가치관 톡 리스트 조회", description = "서비스에 등록된 모든 가치관 톡 질문을 조회합니다.", tags = {"ValueTalk"})
    @ApiResponse(responseCode = "200", description = "가치관 톡 리스트가 성공적으로 조회되었습니다.")
    public ResponseEntity<CommonResponse<ValueTalkResponses>> getValueTalks() {
        List<ValueTalk> valueTalks = valueTalkService.getAllValueTalksActive();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.createSuccess(ValueTalkResponses.from(valueTalks)));
    }

}
