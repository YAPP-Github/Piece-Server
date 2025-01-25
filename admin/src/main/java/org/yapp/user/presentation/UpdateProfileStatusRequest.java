package org.yapp.user.presentation;

import jakarta.validation.constraints.NotNull;

public record UpdateProfileStatusRequest(@NotNull boolean rejectImage,
                                         @NotNull boolean rejectDescription) {

}
