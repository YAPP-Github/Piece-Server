package org.yapp.domain.value.presentation.dto.response;

import java.util.List;
import org.yapp.domain.value.ValuePick;

public record ValuePickResponse(Long id, String category, String question,
                                List<ValuePickAnswerResponse> answers) {

    public static ValuePickResponse from(ValuePick valuePick) {

        List<ValuePickAnswerResponse> answerList = valuePick.getAnswers().entrySet()
            .stream()
            .map(entry -> new ValuePickAnswerResponse(entry.getKey(), (String) entry.getValue()))
            .toList();

        return new ValuePickResponse(valuePick.getId(), valuePick.getCategory(),
            valuePick.getQuestion(),
            answerList);
    }
}