package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public class ProfileRenewedEvent extends ProfileEvent {
    public ProfileRenewedEvent(Long profileId, String nickname) {
        super(profileId, nickname);
    }
}