package org.yapp.domain.value.presentation.dto.response;

import org.yapp.domain.value.ValueTalk;

public record ValueTalkResponse(Long id, String category, String title) {

    public static ValueTalkResponse from(ValueTalk valueTalk) {
        return new ValueTalkResponse(valueTalk.getId(), valueTalk.getCategory(),
            valueTalk.getTitle());
    }
}