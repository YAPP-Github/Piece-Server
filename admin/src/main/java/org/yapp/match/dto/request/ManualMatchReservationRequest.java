package org.yapp.match.dto.request;

import java.time.LocalDateTime;

public record ManualMatchReservationRequest(
    Long user1Id,
    Long user2Id,
    LocalDateTime dateTime
) {

}
