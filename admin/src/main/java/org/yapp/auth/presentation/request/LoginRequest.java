package org.yapp.auth.presentation.request;

import jakarta.validation.constraints.NotNull;
import org.yapp.auth.application.dto.LoginDto;

public record LoginRequest(@NotNull String loginId, @NotNull String password) {

    public LoginDto toDto() {
        return new LoginDto(loginId, password);
    }
}
