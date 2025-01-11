package org.yapp.domain.block.application.dto;

import java.util.List;

public record BlockCreateDto(Long userId, List<String> phoneNumbers) {
    public BlockCreateDto(Long userId, List<String> phoneNumbers) {
        this.userId = userId;
        this.phoneNumbers = phoneNumbers.stream()
                .map(phone -> phone.replaceAll("-", ""))
                .toList();
    }
}
