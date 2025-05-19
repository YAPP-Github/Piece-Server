package org.yapp.core.domain.profile.event;

import lombok.Getter;

@Getter
public class ProfileValueTalkUpdatedEvent extends ProfileEvent {
    public ProfileValueTalkUpdatedEvent(Long profileId, String nickname) {
        super(profileId, nickname);
    }
}