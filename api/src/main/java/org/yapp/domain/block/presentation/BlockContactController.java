package org.yapp.domain.block.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.block.BlockContact;
import org.yapp.domain.block.application.BlockContactService;
import org.yapp.domain.block.application.dto.BlockContactCreateDto;
import org.yapp.domain.block.presentation.dto.request.BlockPhoneNumbersRequest;
import org.yapp.domain.block.presentation.dto.response.UserBlockContactResponses;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blockContacts")
public class BlockContactController {

    private final BlockContactService blockContactService;

    @PostMapping("")
    @Operation(summary = "핸드폰 번호 차단", description = "핸드폰 번호 리스트를 전달받고, 전달받은 핸드폰 번호 차단을 수행합니다.", tags = {
        "차단"})
    @ApiResponse(responseCode = "200", description = "핸드폰 차단 성공")
    public ResponseEntity<CommonResponse<Void>> blockPhoneNumbers(
        @AuthenticationPrincipal Long userId, @RequestBody BlockPhoneNumbersRequest request) {
        blockContactService.blockPhoneNumbers(
            new BlockContactCreateDto(userId, request.phoneNumbers()));
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccessWithNoContent("핸드폰 번호 차단 성공"));
    }

    @GetMapping("")
    @Operation(summary = "핸드폰 번호 차단", description = "차단된 핸드폰 번호 리스트를 조회합니다.", tags = {
        "차단"})
    @ApiResponse(responseCode = "200", description = "차단 핸드폰 번호 조회")
    public ResponseEntity<CommonResponse<UserBlockContactResponses>> blockPhoneNumbers(
        @AuthenticationPrincipal Long userId) {
        List<BlockContact> blockContacts = blockContactService.findBlocksByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(UserBlockContactResponses.from(userId,
                blockContacts)));
    }
}
