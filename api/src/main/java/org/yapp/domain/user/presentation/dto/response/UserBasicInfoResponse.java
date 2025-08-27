package org.yapp.domain.user.presentation.dto.response;


public record UserBasicInfoResponse(Long userId,
                                    String role,
                                    String profileStatus,
                                    Boolean hasRoleChanged,
                                    String accessToken,
                                    String refreshToken) {

}
