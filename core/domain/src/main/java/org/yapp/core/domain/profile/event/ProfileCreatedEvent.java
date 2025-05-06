package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public class ProfileCreatedEvent extends ProfileEvent {
    public ProfileCreatedEvent(Long profileId, String nickname) {
        super(profileId, nickname);
    }
}