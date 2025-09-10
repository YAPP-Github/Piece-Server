package org.yapp.domain.promotion.application.query.promotionValid;

import org.yapp.core.domain.promotion.PromotionTarget;
import org.yapp.core.domain.user.User;

public interface PromotionValidationStrategy {

    PromotionTarget getTarget();


    boolean isEligible(User user);
}