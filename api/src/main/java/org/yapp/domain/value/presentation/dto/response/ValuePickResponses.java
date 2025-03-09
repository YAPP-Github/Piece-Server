package org.yapp.domain.value.presentation.dto.response;

import java.util.List;
import org.yapp.core.domain.value.ValuePick;

public record ValuePickResponses(List<ValuePickResponse> responses) {

    public static ValuePickResponses from(List<ValuePick> valuePicks) {
        List<ValuePickResponse> responseList = valuePicks.stream().map(ValuePickResponse::from)
            .toList();

        return new ValuePickResponses(responseList);
    }
}
