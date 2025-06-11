package org.yapp.domain.match.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record InstantMatchResponse(
    Long matchId,
    Long matchedUserId,
    String matchStatus,
    String description,
    String nickname,
    String birthYear,
    String location,
    String job,
    Integer matchedValueCount,
    List<String> matchedValueList,
    boolean isBlocked,
    LocalDateTime matchedDateTime
) {

}
