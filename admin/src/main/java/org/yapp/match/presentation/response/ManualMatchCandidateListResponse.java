package org.yapp.match.presentation.response;

import java.util.List;

public record ManualMatchCandidateListResponse(
    List<ManualMatchCandidateResponse> candidateList) {

}
