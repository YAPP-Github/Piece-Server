package org.yapp.domain.profile.presentation.request;

import java.util.List;
public record ProfileValueUpdateRequest(List<ProfileValuePair> profileValuePairs) {
}
