package org.yapp.core.domain.kpi.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserActivityDetectedEvent {

    Long userId;
}
