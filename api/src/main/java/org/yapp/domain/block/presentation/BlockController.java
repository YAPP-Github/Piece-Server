package org.yapp.domain.block.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yapp.domain.block.application.BlockService;
import org.yapp.domain.block.application.dto.BlockCreateDto;
import org.yapp.domain.block.presentation.dto.request.BlockPhoneNumbersRequest;
import org.yapp.util.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blocks")
public class BlockController {
    private  final BlockService blockService;

    @PostMapping("")
    @Operation(summary = "핸드폰 번호 차단", description = "핸드폰 번호 리스트를 전달받고, 전달받은 핸드폰 번호 차단을 수행합니다.", tags = {"Block"})
    @ApiResponse(responseCode = "200", description = "핸드폰 차단 성공")
    public ResponseEntity<CommonResponse<Void>> blockPhoneNumbers(@AuthenticationPrincipal Long userId, @RequestBody BlockPhoneNumbersRequest request) {
        blockService.blockPhoneNumbers(new BlockCreateDto(userId, request.phoneNumbers()));
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.createSuccessWithNoContent("핸드폰 번호 차단 성공"));
    }

}
