package org.yapp.domain.value.presentation.dto;

import org.yapp.domain.value.ValueTalk;

import java.util.List;

public record ValueTalkResponses(List<ValueTalkResponse> responses) {
  public static ValueTalkResponses from(List<ValueTalk> valueTalks) {
    List<ValueTalkResponse> responses = valueTalks.stream().map(ValueTalkResponse::from).toList();
    return new ValueTalkResponses(responses);
  }
}