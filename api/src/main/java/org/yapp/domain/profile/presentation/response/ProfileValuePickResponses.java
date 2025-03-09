package org.yapp.domain.profile.presentation.response;

import java.util.List;
import java.util.Map;
import org.yapp.core.domain.profile.ProfileValuePick;
import org.yapp.core.domain.value.ValuePick;
import org.yapp.domain.value.presentation.dto.response.ValuePickAnswerResponse;

public record ProfileValuePickResponses(List<ProfileValuePickResponse> responses) {

    public static ProfileValuePickResponses from(List<ValuePick> valuePicks,
        Map<Long, ProfileValuePick> profileValuePicks) {

        return new ProfileValuePickResponses(
            valuePicks.stream()
                .map(
                    valuePick -> mapToResponse(valuePick, profileValuePicks.get(valuePick.getId())))
                .toList()
        );
    }

    private static ProfileValuePickResponse mapToResponse(ValuePick valuePick,
        ProfileValuePick profileValuePick) {
        List<ValuePickAnswerResponse> answerList = valuePick.getAnswers().entrySet()
            .stream()
            .map(entry -> new ValuePickAnswerResponse(entry.getKey(), (String) entry.getValue()))
            .toList();

        Integer selectedAnswer =
            profileValuePick != null ? profileValuePick.getSelectedAnswer() : null;

        return new ProfileValuePickResponse(
            valuePick.getCategory(),
            valuePick.getQuestion(),
            answerList,
            profileValuePick != null ? profileValuePick.getId() : null,
            selectedAnswer
        );
    }
}
