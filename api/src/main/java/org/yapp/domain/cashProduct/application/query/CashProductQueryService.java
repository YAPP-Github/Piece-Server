package org.yapp.domain.cashProduct.application.query;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.cashProduct.application.dto.CashProductQueryResult;
import org.yapp.domain.cashProduct.dao.CashProductRepository;

@Service
@RequiredArgsConstructor
public class CashProductQueryService {

    private final CashProductRepository cashProductRepository;

    @Transactional(readOnly = true)
    public List<CashProductQueryResult> getCashProductsIsActive() {
        return cashProductRepository.findAllByIsActive(true).stream()
            .map(CashProductQueryResult::from)
            .collect(Collectors.toList());
    }
}
