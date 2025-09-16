package org.yapp.domain.payment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yapp.core.domain.payment.common.entity.Payment;
import org.yapp.core.domain.user.User;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    boolean existsByUser(User user);
}
