package org.yapp.domain.value.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.yapp.domain.value.ValueTalk;
import org.yapp.domain.value.dao.ValueTalkRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValueTalkService {
    private final ValueTalkRepository valueTalkRepository;

    public List<ValueTalk> getAllValueTalksActive() {
        return valueTalkRepository.findAllActiveOrdered();
    }
}
