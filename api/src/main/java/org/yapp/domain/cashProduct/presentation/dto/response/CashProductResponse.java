package org.yapp.domain.cashProduct.presentation.dto.response;

import java.math.BigDecimal;
import org.yapp.domain.cashProduct.application.dto.CashProductQueryResult;

public record CashProductResponse(
    String uuid,
    String name,
    Long rewardPuzzleCount,
    BigDecimal originalAmount,
    String currencyCode,
    BigDecimal discountRate,
    BigDecimal discountedAmount) {

    public static CashProductResponse from(CashProductQueryResult cashProductDto) {
        return new CashProductResponse(
            cashProductDto.uuid(),
            cashProductDto.name(),
            cashProductDto.rewardPuzzle().getCount(),
            cashProductDto.price().getAmount().stripTrailingZeros(),
            cashProductDto.price().getCurrency().getCurrencyCode(),
            cashProductDto.discountRate(),
            cashProductDto.price().applyPercentageDiscount(cashProductDto.discountRate())
                .getAmount());
    }
}
