package org.yapp.domain.payment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yapp.core.domain.payment.inApp.enums.InAppStore;
import org.yapp.domain.payment.application.command.InAppPurchaseService;
import org.yapp.domain.payment.presentation.request.InAppPaymentCreateRequest;
import org.yapp.format.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments/in-app")
public class InAppPaymentController {

    private final InAppPurchaseService inAppPurchaseService;

    @PreAuthorize(value = "hasAuthority('USER')")
    @PostMapping()
    @Operation(summary = "인앱 결제", description = "인앱 결제를 진행합니다.", tags = {
            "프로필" })
    @ApiResponse(responseCode = "200", description = "인앱 결제가 성공적으로 진행되었습니다.")
    public ResponseEntity<CommonResponse<Void>> createInAppPayment(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid InAppPaymentCreateRequest request) {
        InAppStore store = request.store();

        inAppPurchaseService.execute(userId, request.productUUID(), store.createPurchaseCredential(
                request.purchaseCredential()));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.createSuccess(null));
    }

}
