package org.yapp.domain.profile.presentation.request;

import jakarta.validation.constraints.Size;
import java.util.List;

public record ProfileValueTalkUpdateRequest(
    List<ProfileValueTalkPair> profileValueTalkUpdateRequests
) {

    public static record ProfileValueTalkPair(Long profileValueTalkId,
                                              @Size(max = 300) String answer,
                                              String summary) {

    }
}
