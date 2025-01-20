package org.yapp.domain.profile.presentation.request;

import java.util.List;

public record ProfileValuePickUpdateRequest(
    List<ProfileValuePickPair> profileValuePickUpdateRequests) {

    public static record ProfileValuePickPair(Long profileValuePickId, Integer selectedAnswer) {

    }
}
