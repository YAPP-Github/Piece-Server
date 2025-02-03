package org.yapp.core.sse;

import org.yapp.core.sse.dto.SsePayload;

public interface SsePersonalService {

    public Object connect(Long userId);

    public void sendToPersonal(Long userId, SsePayload<Object> data);

    public void disconnect(Long userId);
}
