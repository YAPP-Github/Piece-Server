package org.yapp.domain.profile.presentation.response;

import org.yapp.domain.profile.ProfileValuePick;

public record ProfileValuePickResponse(
    Long id,
    Long valuePickId,
    String question,
    Integer selectedAnswer
) {

    public ProfileValuePickResponse(ProfileValuePick profileValuePick) {
        this(
            profileValuePick.getId(),
            profileValuePick.getValuePick().getId(),
            profileValuePick.getValuePick().getQuestion(),
            profileValuePick.getSelectedAnswer()
        );
    }
}