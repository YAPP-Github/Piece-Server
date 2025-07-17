package org.yapp.domain.cashProduct.presentation.dto.response;

import java.util.List;
import org.yapp.domain.cashProduct.application.dto.CashProductQueryResult;

public record CashProductResponses(List<CashProductResponse> responses) {

    public static CashProductResponses from(List<CashProductQueryResult> cashProductsDto) {
        List<CashProductResponse> responses =
            cashProductsDto.stream()
                .map(CashProductResponse::from)
                .toList();

        return new CashProductResponses(responses);
    }
}