package org.yapp.domain.promotion.application.query.promotionValid;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.yapp.core.domain.promotion.PromotionTarget;
import org.yapp.core.domain.user.User;


@Component
public class PromotionValidator {

    private final Map<PromotionTarget, PromotionValidationStrategy> strategyMap;

    public PromotionValidator(List<PromotionValidationStrategy> strategies) {
        this.strategyMap = new EnumMap<>(PromotionTarget.class);
        for (PromotionValidationStrategy strategy : strategies) {
            this.strategyMap.put(strategy.getTarget(), strategy);
        }
    }

    public boolean validate(User user, PromotionTarget target) {
        PromotionValidationStrategy strategy = strategyMap.get(target);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for target: " + target);
        }
        return strategy.isEligible(user);
    }
}
