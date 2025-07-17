package org.yapp.domain.cashProduct.presentation;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.domain.cashProduct.application.dto.CashProductQueryResult;
import org.yapp.domain.cashProduct.application.query.CashProductQueryService;
import org.yapp.domain.cashProduct.presentation.dto.response.CashProductResponses;
import org.yapp.format.CommonResponse;

@RestController
@RequestMapping("/api/cash-products")
@RequiredArgsConstructor
public class CashProductController {

    private final CashProductQueryService cashProductQueryService;

    @GetMapping
    @Operation(summary = "현금 결제 상품 조회", description = "구매 가능한 현금 상품을 모두 조회합니다.", tags = {"현금 상품"})
    public ResponseEntity<CommonResponse<CashProductResponses>> getCashProducts() {
        List<CashProductQueryResult> cashProductQueryResults = cashProductQueryService.getCashProductsIsActive();

        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponse.createSuccess(CashProductResponses.from(cashProductQueryResults)));
    }
}
