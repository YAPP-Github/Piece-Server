package org.yapp.domain.promotion.application.dto;

import java.math.BigDecimal;
import org.yapp.core.domain.common.vo.Money;
import org.yapp.core.domain.common.vo.Puzzle;
import org.yapp.core.domain.promotion.PromotionCashProduct;

public record PromotionCashProductQueryResult(
    Long id,
    String uuid,
    String name,
    Money price,
    BigDecimal discountRate,
    Boolean isActive,
    Puzzle rewardPuzzle,
    String cardImageUrl) {

    public static PromotionCashProductQueryResult from(PromotionCashProduct promotionCashProduct) {
        return new PromotionCashProductQueryResult(
            promotionCashProduct.getId(),
            promotionCashProduct.getUuid(),
            promotionCashProduct.getName(),
            promotionCashProduct.getPrice(),
            promotionCashProduct.getDiscountRate(),
            promotionCashProduct.getIsActive(),
            promotionCashProduct.getRewardPuzzle(),
            promotionCashProduct.getCardImageUrl());
    }
}
