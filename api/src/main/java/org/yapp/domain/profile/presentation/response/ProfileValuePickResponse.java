package org.yapp.domain.profile.presentation.response;

import java.util.List;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.presentation.dto.response.ValuePickAnswerResponse;


public record ProfileValuePickResponse(Long id, String category, String question,
                                       List<ValuePickAnswerResponse> answers,
                                       Integer selectedAnswer) {

    public static ProfileValuePickResponse from(
        ValuePick valuePick, Integer selectedAnswer) {

        List<ValuePickAnswerResponse> answerList = valuePick.getAnswers().entrySet()
            .stream()
            .map(entry -> new ValuePickAnswerResponse(entry.getKey(), (String) entry.getValue()))
            .toList();

        return new ProfileValuePickResponse(valuePick.getId(), valuePick.getCategory(),
            valuePick.getQuestion(),
            answerList, selectedAnswer);
    }
}