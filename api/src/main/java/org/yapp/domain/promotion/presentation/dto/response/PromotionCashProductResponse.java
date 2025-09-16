package org.yapp.domain.promotion.presentation.dto.response;

import org.yapp.domain.promotion.application.dto.PromotionCashProductQueryResult;

public record PromotionCashProductResponse(
    String uuid,
    String cardImageUrl) {

    public static PromotionCashProductResponse from(
        PromotionCashProductQueryResult dto) {
        return new PromotionCashProductResponse(
            dto.uuid(),
            dto.cardImageUrl());
    }
}
