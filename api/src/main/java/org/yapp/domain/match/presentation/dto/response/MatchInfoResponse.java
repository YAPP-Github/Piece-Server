package org.yapp.domain.match.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchInfoResponse {

    private Long matchId;
    private Long matchedUserId;
    private String matchStatus;
    private String description;
    private String nickname;
    private String birthYear;
    private String location;
    private String job;
    private Integer matchedValueCount;
    private List<String> matchedValueList;
    private boolean isBlocked;

    @Builder
    public MatchInfoResponse(Long matchId, Long matchedUserId, String matchStatus,
        String description,
        String nickname,
        String birthYear,
        String location, String job, Integer matchedValueCount, List<String> matchedValueList,
        boolean isBlocked) {
        this.matchId = matchId;
        this.matchedUserId = matchedUserId;
        this.matchStatus = matchStatus;
        this.isBlocked = isBlocked;
        this.description = description;
        this.nickname = nickname;
        this.birthYear = birthYear;
        this.location = location;
        this.job = job;
        this.matchedValueCount = matchedValueCount;
        this.matchedValueList = matchedValueList;
    }
}
