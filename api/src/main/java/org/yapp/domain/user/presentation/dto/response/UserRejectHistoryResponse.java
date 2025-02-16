package org.yapp.domain.user.presentation.dto.response;

import org.yapp.core.domain.profile.ProfileStatus;

public record UserRejectHistoryResponse(ProfileStatus profileStatus, boolean reasonImage,
                                        boolean reasonValues) {

}
