package org.yapp.ban.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserBanRequest(@NotNull Long userId) {

}
