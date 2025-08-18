package org.yapp.domain.payment.dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.payment.inApp.entity.InAppPayment;

@Repository
public interface InAppPaymentRepository extends JpaRepository<InAppPayment, Long> {

    Optional<InAppPayment> findByPurchaseId(String purchaseId);

}
