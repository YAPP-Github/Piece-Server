package org.yapp.domain.promotion.application.query;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.promotion.PromotionCashProduct;
import org.yapp.core.domain.user.User;
import org.yapp.domain.promotion.application.dto.PromotionCashProductQueryResult;
import org.yapp.domain.promotion.application.query.promotionValid.PromotionValidator;
import org.yapp.domain.promotion.dao.PromotionCashProductRepository;

@Service
@RequiredArgsConstructor
public class PromotionCashProductQueryService {

    private final PromotionCashProductRepository promotionCashProductRepository;
    private final PromotionValidator promotionValidator;

    @Transactional(readOnly = true)
    public List<PromotionCashProductQueryResult> getPromotionCashProductsByUserId(Long userId) {
        List<PromotionCashProduct> promotionCashProducts = getPromotionCashProducts();

        return promotionCashProducts.stream()
            .filter(promotionCashProduct -> this.checkUserAppliedPromotion(userId,
                promotionCashProduct))
            .map(
                PromotionCashProductQueryResult::from
            ).collect(Collectors.toList());
    }

    private List<PromotionCashProduct> getPromotionCashProducts() {
        return promotionCashProductRepository.findAllByStartDateBeforeAndEndDateAfterAndIsActive(
            LocalDate.now(),
            LocalDate.now(), true);
    }

    private boolean checkUserAppliedPromotion(Long userId,
        PromotionCashProduct promotionCashProduct) {
        User user = User.builder()
            .id(userId)
            .build();

        return promotionValidator.validate(user, promotionCashProduct.getPromotionTarget());
    }
}
