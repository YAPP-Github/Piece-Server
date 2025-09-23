package org.yapp.domain.block.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.block.application.BloomBlockRepairService;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block/repair")
public class BlockRepairController {

    private final BloomBlockRepairService blockRepairService;

    @PostMapping("/{targetUserId}")
    @Operation(summary = "특정 사용자 블룸 필터 복구", description = "특정 사용자의 REDIS 블룸 필터를 복구합니다.", tags = {
        "차단"})
    @ApiResponse(responseCode = "200", description = "복구 성공")
    public ResponseEntity<CommonResponse<Void>> repairBlock(
        @AuthenticationPrincipal Long adminUserId,
        @PathVariable Long targetUserId) {
        blockRepairService.repairBloomBlockByUserId(adminUserId, targetUserId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccessWithNoContent("블룸 필터 복구 성공"));
    }

    @PostMapping("/all")
    @Operation(summary = "모두의 블룸 필터 복구", description = "REDIS 장애나 마이그레이션 상황에 쓰는 필터 복구 API 입니다.", tags = {
        "차단"})
    @ApiResponse(responseCode = "200", description = "복구 성공")
    public ResponseEntity<CommonResponse<Void>> repairAllBlock(
        @AuthenticationPrincipal Long userId) {
        blockRepairService.repairBloomBlockForAllUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccessWithNoContent("블룸 필터 복구 성공"));
    }
}
