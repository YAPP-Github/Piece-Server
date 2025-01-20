package org.yapp.block.presentation.response;

import java.time.LocalDate;

public record UserBlockResponse(
    Long blockedUserId,
    String BlockedUserNickname, String BlockedUserName,
    LocalDate blockedUserBirthdate, Long blockingUserId,
    String blockingUserNickname,
    String blockingUserName, LocalDate BlockedDate) {

}
