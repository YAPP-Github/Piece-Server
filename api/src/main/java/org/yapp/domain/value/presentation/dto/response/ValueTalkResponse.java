package org.yapp.domain.value.presentation.dto.response;

import java.util.List;
import org.yapp.core.domain.value.ValueTalk;

public record ValueTalkResponse(Long id, String category, String title, String placeholder,
                                List<String> guides) {

    public static ValueTalkResponse from(ValueTalk valueTalk) {
        return new ValueTalkResponse(valueTalk.getId(), valueTalk.getCategory(),
            valueTalk.getTitle(), valueTalk.getPlaceholder(), valueTalk.getGuides());
    }
}