package org.yapp.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.user.application.UserService;
import org.yapp.domain.user.presentation.response.UserRejectHistoryResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize(value = "hasRole('USER')")
    @GetMapping("/reject")
    @Operation(summary = "사용자 거절 사유 조회", description = "사용자의 최근 거절 사유를 조회합니다.", tags = {"사용자"})
    public ResponseEntity<CommonResponse<UserRejectHistoryResponse>> getUserRejectHistory(
        @AuthenticationPrincipal Long userId) {
        UserRejectHistoryResponse response = userService.getUserRejectHistoryLatest(
            userId);
        
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(response));
    }
}
