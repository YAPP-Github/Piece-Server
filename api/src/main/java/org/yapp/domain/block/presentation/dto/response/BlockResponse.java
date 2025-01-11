package org.yapp.domain.block.presentation.dto.response;

import java.time.LocalDateTime;

public record BlockResponse(String phoneNumber, LocalDateTime blockedAt) {
}
