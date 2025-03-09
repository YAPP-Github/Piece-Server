package org.yapp.domain.profile.presentation.response;

import java.util.List;
import org.yapp.domain.value.presentation.dto.response.ValuePickAnswerResponse;


public record ProfileValuePickResponse(String category, String question,
                                       List<ValuePickAnswerResponse> answers,
                                       Long profileValuePickId,
                                       Integer selectedAnswer) {

}