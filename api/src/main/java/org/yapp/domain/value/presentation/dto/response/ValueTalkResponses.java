package org.yapp.domain.value.presentation.dto.response;

import java.util.List;
import org.yapp.core.domain.value.ValueTalk;

public record ValueTalkResponses(List<ValueTalkResponse> responses) {

    public static ValueTalkResponses from(List<ValueTalk> valueTalks) {
        List<ValueTalkResponse> responses = valueTalks.stream().map(ValueTalkResponse::from)
            .toList();
        return new ValueTalkResponses(responses);
    }
}