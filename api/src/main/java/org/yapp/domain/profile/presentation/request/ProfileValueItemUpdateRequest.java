package org.yapp.domain.profile.presentation.request;

import java.util.List;
public record ProfileValueItemUpdateRequest(List<ProfileValueItemPair> profileValueItemPairs) {
}
