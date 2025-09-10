package org.yapp.domain.promotion.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.promotion.PromotionCashProduct;

@Repository
public interface PromotionCashProductRepository extends JpaRepository<PromotionCashProduct, Long> {

    List<PromotionCashProduct> findAllByStartDateBeforeAndEndDateAfterAndIsActive(
        LocalDate startDateBefore, LocalDate endDateAfter,
        Boolean isActive);

}
