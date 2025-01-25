package org.yapp.domain.block.application.dto;

import java.util.List;

public record BlockContactCreateDto(Long userId, List<String> phoneNumbers) {

    public BlockContactCreateDto(Long userId, List<String> phoneNumbers) {
        this.userId = userId;
        this.phoneNumbers = phoneNumbers.stream()
            .map(phone -> phone.replaceAll("-", ""))
            .toList();
    }
}
