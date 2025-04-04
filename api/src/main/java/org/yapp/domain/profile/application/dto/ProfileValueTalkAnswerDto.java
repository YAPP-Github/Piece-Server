package org.yapp.domain.profile.application.dto;

import java.util.List;
import org.yapp.domain.profile.presentation.request.ProfileValueTalkUpdateRequest;

public record ProfileValueTalkAnswerDto(
    Long profileValueTalkId, String answer
) {

    public static List<ProfileValueTalkAnswerDto> from(ProfileValueTalkUpdateRequest request) {
        return request.profileValueTalkUpdateRequests().stream().map(
            (p) -> new ProfileValueTalkAnswerDto(
                p.profileValueTalkId(), p.answer())
        ).toList();
    }
};
