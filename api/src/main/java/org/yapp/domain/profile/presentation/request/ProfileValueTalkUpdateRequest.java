package org.yapp.domain.profile.presentation.request;

import java.util.List;

public record ProfileValueTalkUpdateRequest(
    List<ProfileValueTalkPair> profileValueTalkUpdateRequests
) {

}
