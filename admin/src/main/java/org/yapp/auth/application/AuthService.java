package org.yapp.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yapp.auth.AuthToken;
import org.yapp.auth.AuthTokenGenerator;
import org.yapp.auth.application.dto.LoginDto;
import org.yapp.domain.user.User;
import org.yapp.error.code.auth.AuthErrorCode;
import org.yapp.error.exception.ApplicationException;
import org.yapp.user.application.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final AuthTokenGenerator authTokenGenerator;

    @Value("${admin.password}")
    private String PWD;

    public AuthToken login(LoginDto loginDto) {
        String oauthId = loginDto.oauthId();
        String password = loginDto.password();

        User loginUser = userService.getUserByOauthId(oauthId);

        if (password.equals(PWD) && loginUser.getRole().equals(AdminRole.ADMIN.toString())) {
            return authTokenGenerator.generate(loginUser.getId(), null,
                AdminRole.ROLE_ADMIN.toString());
        } else {
            throw new ApplicationException(AuthErrorCode.ACCESS_DENIED);
        }
    }
}
