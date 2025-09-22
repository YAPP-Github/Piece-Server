package org.yapp.infra.billing.playstore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseType {
    TEST(0),
    PROMO(1),
    REWARDED(2);

    private final int value;
}