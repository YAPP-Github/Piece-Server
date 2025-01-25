package org.yapp.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.auth.AuthToken;
import org.yapp.auth.application.AuthService;
import org.yapp.auth.presentation.request.LoginRequest;
import org.yapp.util.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/auth/")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthToken>> login(
        @RequestBody @Valid LoginRequest loginRequest) {
        AuthToken authToken = authService.login(loginRequest.toDto());
        return ResponseEntity.ok(CommonResponse.createSuccess(authToken));
    }
}
