package org.yapp.domain.promotion.application.query.promotionValid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.promotion.PromotionTarget;
import org.yapp.core.domain.user.User;
import org.yapp.domain.payment.application.PaymentHistoryService;

@Component
@RequiredArgsConstructor
public class FirstPurchaseUserValidationStrategy implements PromotionValidationStrategy {

    private final PaymentHistoryService paymentHistoryService;

    @Override
    public PromotionTarget getTarget() {
        return PromotionTarget.FIRST_PURCHASE_USER;
    }

    @Override
    public boolean isEligible(User user) {
        return !paymentHistoryService.hasAnyPaymentHistory(user);
    }
}