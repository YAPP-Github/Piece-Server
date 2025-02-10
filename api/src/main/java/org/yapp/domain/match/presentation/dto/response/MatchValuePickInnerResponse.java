package org.yapp.domain.match.presentation.dto.response;

import java.util.List;
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
  private List<String> answer;
  private Integer selectedAnswer;
}
