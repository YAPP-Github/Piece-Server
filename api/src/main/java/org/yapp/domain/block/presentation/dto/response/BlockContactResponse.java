package org.yapp.domain.block.presentation.dto.response;

import java.time.LocalDateTime;

public record BlockContactResponse(String phoneNumber, LocalDateTime blockedAt) {

}
