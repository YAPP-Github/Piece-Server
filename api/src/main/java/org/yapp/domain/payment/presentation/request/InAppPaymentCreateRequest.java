package org.yapp.domain.payment.presentation.request;

import org.yapp.core.domain.payment.inApp.enums.InAppStore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InAppPaymentCreateRequest(
        @NotBlank String productUUID,
        @NotBlank String purchaseCredential,
        @NotNull InAppStore store) {

}
