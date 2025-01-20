package org.yapp.domain.auth.presentation.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleStatus {
    NONE("NONE"),
    REGISTER("REGISTER"),
    PENDING("PENDING"),
    USER("USER");
    private String status;

    @JsonValue
    public String getStatus() {
        return status;
    }
}
