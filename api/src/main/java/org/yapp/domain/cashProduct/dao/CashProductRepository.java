package org.yapp.domain.cashProduct.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.product.CashProduct;

@Repository
public interface CashProductRepository extends JpaRepository<CashProduct, Long> {

    List<CashProduct> findAllByIsActive(Boolean isActive);

    Optional<CashProduct> findByUuidAndIsActive(String uuid, Boolean isActive);
}
