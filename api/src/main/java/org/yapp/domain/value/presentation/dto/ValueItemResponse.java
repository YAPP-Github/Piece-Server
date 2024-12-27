package org.yapp.domain.value.presentation.dto;

import org.yapp.domain.value.ValueItem;

import java.util.Map;

public record ValueItemResponse(Long id, String category, String question, Map<Integer, Object> answers) {
  public static ValueItemResponse from(ValueItem valueItem) {
    return new ValueItemResponse(valueItem.getId(), valueItem.getCategory(), valueItem.getQuestion(),
        valueItem.getAnswers());
  }
}