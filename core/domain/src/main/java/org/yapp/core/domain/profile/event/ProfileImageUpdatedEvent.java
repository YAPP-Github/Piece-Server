package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public class ProfileImageUpdatedEvent extends ProfileEvent {

    public ProfileImageUpdatedEvent(Long profileId, String nickname) {
        super(profileId, nickname);
    }
}