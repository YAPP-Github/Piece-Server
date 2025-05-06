package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public abstract class ProfileEvent {
    private final Long profileId;
    private final String nickname;

    protected ProfileEvent(Long profileId, String nickname) {
        this.profileId = profileId;
        this.nickname = nickname;
    }
}