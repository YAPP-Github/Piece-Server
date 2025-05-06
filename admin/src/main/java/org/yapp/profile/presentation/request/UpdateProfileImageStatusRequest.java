package org.yapp.profile.presentation.request;

import jakarta.validation.constraints.NotNull;

public record UpdateProfileImageStatusRequest(@NotNull boolean accepted) {

}
