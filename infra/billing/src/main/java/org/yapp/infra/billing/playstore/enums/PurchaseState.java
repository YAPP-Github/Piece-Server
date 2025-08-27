package org.yapp.infra.billing.playstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseState {
    PURCHASED(0),
    CANCELED(1),
    PENDING(2);

    private final int value;

    public static PurchaseState fromValue(int value) {
        for (PurchaseState state : PurchaseState.values()) {
            if (state.getValue() == value) {
                return state;
            }
        }

        return null;
    }
}