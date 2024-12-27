package org.yapp.domain.value.presentation.dto;

import org.yapp.domain.value.ValueItem;

import java.util.List;

public record ValueItemResponses(List<ValueItemResponse> responses) {
  public static ValueItemResponses from(List<ValueItem> valueItems) {
    List<ValueItemResponse> responseList = valueItems.stream().map(ValueItemResponse::from).toList();

    return new ValueItemResponses(responseList);
  }
}
