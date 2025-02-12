package org.yapp.domain.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yapp.domain.auth.application.authorization.SmsAuthService;
import org.yapp.domain.auth.presentation.dto.request.SmsAuthRequest;
import org.yapp.domain.auth.presentation.dto.request.SmsAuthVerifyRequest;
import org.yapp.domain.auth.presentation.dto.response.OauthLoginResponse;
import org.yapp.domain.user.application.UserService;
import org.yapp.format.CommonResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/register/sms/auth")
public class SmsAuthController {

    private final SmsAuthService smsAuthService;
    private final UserService userService;

    @PostMapping("/code")
    @Operation(summary = "SMS 인증번호 발송", description = "전화번호로 인증번호를 발송합니다.", tags = {"SMS 인증"})
    public ResponseEntity<CommonResponse<Void>> sendSms(
        @RequestBody SmsAuthRequest smsAuthRequest) {
        smsAuthService.sendAuthCodeTo(smsAuthRequest.getPhoneNumber());
        return ResponseEntity.ok(CommonResponse.createSuccessWithNoContent());
    }

    @PostMapping("/code/verify")
    @Operation(summary = "SMS 인증번호 검증", description = "받은 인증번호를 검증합니다.", tags = {"SMS 인증"})
    public ResponseEntity<CommonResponse<OauthLoginResponse>> verifyCode(
        @RequestBody SmsAuthVerifyRequest request,
        @AuthenticationPrincipal Long userId) {
        smsAuthService.verifySmsAuthCode(request.getPhoneNumber(), request.getCode());
        OauthLoginResponse registerToken = userService.registerPhoneNumber(
            userId, request.getPhoneNumber());
        return ResponseEntity.ok(CommonResponse.createSuccess(registerToken));
    }
}
