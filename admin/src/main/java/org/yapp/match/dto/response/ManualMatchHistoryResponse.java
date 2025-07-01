package org.yapp.match.dto.response;

import java.time.LocalDateTime;

public record ManualMatchHistoryResponse(
    Long user1Id,
    String user1Nickname,
    Long user2Id,
    String user2Nickname,
    LocalDateTime matchDateTime,
    Boolean isMatched
) {

}
