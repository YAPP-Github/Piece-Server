package org.yapp.domain.value.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.domain.value.ValueTalk;
import org.yapp.domain.value.dao.ValueTalkRepository;

@Service
@RequiredArgsConstructor
public class ValueTalkService {

    private final ValueTalkRepository valueTalkRepository;

    public List<ValueTalk> getAllActiveValueTalks() {
        return valueTalkRepository.findAllActiveOrdered();
    }
}
