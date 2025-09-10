package org.yapp.domain.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.user.User;
import org.yapp.domain.payment.dao.PaymentRepository;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public boolean hasAnyPaymentHistory(User user) {
        return paymentRepository.existsByUser(user);
    }

    @Transactional(readOnly = true)
    public boolean hasAnyPaymentHistory(Long userId) {
        User user = User.builder().id(userId).build();
        return hasAnyPaymentHistory(user);
    }
}