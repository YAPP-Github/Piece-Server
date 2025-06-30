package org.yapp.match.dto.response;

import java.util.List;

public record ManualMatchCandidateListResponse(
    List<ManualMatchCandidateResponse> candidateList) {

}
