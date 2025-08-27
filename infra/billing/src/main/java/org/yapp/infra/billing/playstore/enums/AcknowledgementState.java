package org.yapp.infra.billing.playstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcknowledgementState {
    NOT_YET_ACKNOWLEDGED(0),
    ACKNOWLEDGED(1);

    private final int value;
}