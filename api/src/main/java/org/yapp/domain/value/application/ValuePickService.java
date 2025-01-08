package org.yapp.domain.value.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.dao.ValuePickRepository;

@Service
@RequiredArgsConstructor
public class ValuePickService {

    private final ValuePickRepository valuePickRepository;

    public List<ValuePick> getAllActiveValuePicks() {
        return valuePickRepository.findAllByIsActiveTrue();
    }
}
