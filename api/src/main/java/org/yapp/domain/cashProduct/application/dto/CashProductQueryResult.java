package org.yapp.domain.cashProduct.application.dto;

import java.math.BigDecimal;
import org.yapp.core.domain.common.vo.Money;
import org.yapp.core.domain.common.vo.Puzzle;
import org.yapp.core.domain.product.CashProduct;

public record CashProductQueryResult(
    Long id,
    String uuid,
    String name,
    Money price,
    BigDecimal discountRate,
    Boolean isActive,
    Puzzle rewardPuzzle) {

    public static CashProductQueryResult from(CashProduct cashProduct) {
        return new CashProductQueryResult(
            cashProduct.getId(),
            cashProduct.getUuid(),
            cashProduct.getName(),
            cashProduct.getPrice(),
            cashProduct.getDiscountRate(),
            cashProduct.getIsActive(),
            cashProduct.getRewardPuzzle());
    }
}
