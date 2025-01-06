package org.yapp.domain.value.presentation.dto;

import org.yapp.domain.value.ValuePick;

import java.util.Map;

public record ValuePickResponse(Long id, String category, String question, Map<Integer, Object> answers) {
  public static ValuePickResponse from(ValuePick valuePick) {
    return new ValuePickResponse(valuePick.getId(), valuePick.getCategory(), valuePick.getQuestion(),
        valuePick.getAnswers());
  }
}