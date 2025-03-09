package org.yapp.domain.match.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MatchValueTalkInnerResponse {

  private String category;
  private String summary;
  private String answer;

}
