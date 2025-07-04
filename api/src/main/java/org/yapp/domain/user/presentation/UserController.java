package org.yapp.domain.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.auth.application.oauth.service.OauthService;
import org.yapp.domain.user.application.UserService;
import org.yapp.domain.user.presentation.dto.request.FcmTokenSaveRequest;
import org.yapp.domain.user.presentation.dto.request.OauthUserDeleteRequest;
import org.yapp.domain.user.presentation.dto.request.UserDeleteRequest;
import org.yapp.domain.user.presentation.dto.response.UserBasicInfoResponse;
import org.yapp.domain.user.presentation.dto.response.UserRejectHistoryResponse;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final OauthService oauthService;

    @GetMapping("/reject")
    @PreAuthorize(value = "hasAnyAuthority('PENDING')")
    @Operation(summary = "사용자 거절 사유 조회", description = "사용자의 최근 거절 사유를 조회합니다.", tags = {"사용자"})
    public ResponseEntity<CommonResponse<UserRejectHistoryResponse>> getUserRejectHistory(
        @AuthenticationPrincipal Long userId) {
        UserRejectHistoryResponse response = userService.getUserRejectHistoryLatest(
            userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(response));
    }

    @DeleteMapping
    @PreAuthorize(value = "hasAnyAuthority('REGISTER','PENDING','USER')")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다.", tags = {"사용자"})
    public ResponseEntity<CommonResponse<Void>> deleteUser(
        @RequestBody @Valid UserDeleteRequest request,
        @AuthenticationPrincipal Long userId) {
        userService.deleteUser(userId, request.getReason());
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @DeleteMapping("/oauth")
    @PreAuthorize(value = "hasAnyAuthority('REGISTER','PENDING','USER')")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다.", tags = {"사용자"})
    public ResponseEntity<CommonResponse<Void>> deleteUser(
        @RequestBody @Valid OauthUserDeleteRequest request,
        @AuthenticationPrincipal Long userId) {

        oauthService.withdraw(request, userId);
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @GetMapping("/info")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "회원 기본정보 확인", description = "회원의 기본 정보를 확인합니다", tags = {"사용자"})
    public ResponseEntity<CommonResponse<UserBasicInfoResponse>> getUserBasicInfo(
        @AuthenticationPrincipal Long userId,
        @RequestHeader("Authorization") String authorizationHeader) {
        UserBasicInfoResponse userBasicInfo =
            userService.getUserBasicInfo(userId, authorizationHeader);
        return ResponseEntity.ok(CommonResponse.createSuccess(userBasicInfo));
    }

    @PostMapping("/fcm-token")
    @PreAuthorize(value = "isAuthenticated()")
    @Operation(summary = "FCM 토큰 등록", description = "FCM 토큰을 등록합니다", tags = {"사용자"})
    public ResponseEntity<CommonResponse<Void>> saveFcmToken(
        @AuthenticationPrincipal Long userId,
        @RequestBody FcmTokenSaveRequest request) {
        userService.saveFcmToken(userId, request);
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }
}
