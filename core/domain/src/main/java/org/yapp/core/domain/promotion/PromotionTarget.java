package org.yapp.core.domain.promotion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PromotionTarget {

    FIRST_PURCHASE_USER("첫 구매 회원");

    private final String description;
}
