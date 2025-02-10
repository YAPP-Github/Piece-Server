package org.yapp.domain.match.presentation.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class MatchValuePickInnerResponse {

  private String category;
  private String question;
  private Boolean isSameWithMe;
  private Map<Integer, Object> answer;
  private Integer selectedAnswer;
}
