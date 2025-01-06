package org.yapp.domain.value.presentation.dto;

import org.yapp.domain.value.ValuePick;

import java.util.List;

public record ValuePickResponses(List<ValuePickResponse> responses) {
  public static ValuePickResponses from(List<ValuePick> valuePicks) {
    List<ValuePickResponse> responseList = valuePicks.stream().map(ValuePickResponse::from).toList();

    return new ValuePickResponses(responseList);
  }
}
