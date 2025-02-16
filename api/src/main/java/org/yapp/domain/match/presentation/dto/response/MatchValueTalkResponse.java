package org.yapp.domain.match.presentation.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MatchValueTalkResponse {

  private Long matchId;
  private Long matchedUserId;
  private String description;
  private String nickname;
  private List<MatchValueTalkInnerResponse> valueTalks;
}
