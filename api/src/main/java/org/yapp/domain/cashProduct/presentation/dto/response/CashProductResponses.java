package org.yapp.domain.cashProduct.presentation.dto.response;

import java.util.List;
import org.yapp.domain.cashProduct.application.dto.CashProductQueryResult;
import org.yapp.domain.promotion.application.dto.PromotionCashProductQueryResult;
import org.yapp.domain.promotion.presentation.dto.response.PromotionCashProductResponse;

public record CashProductResponses(
        List<CashProductResponse> basicCashProducts,
        List<PromotionCashProductResponse> promotionCashProducts) {

    public static CashProductResponses from(
            List<CashProductQueryResult> cashProductsDto,
            List<PromotionCashProductQueryResult> promotionCashProductsDto) {
        List<CashProductResponse> basicCashProductResponses = cashProductsDto.stream()
                .map(CashProductResponse::from)
                .toList();

        List<PromotionCashProductResponse> promotionCashProductResponses = promotionCashProductsDto.stream()
                .map(PromotionCashProductResponse::from)
                .toList();

        return new CashProductResponses(basicCashProductResponses, promotionCashProductResponses);
    }
}