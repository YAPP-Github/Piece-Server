package org.yapp.domain.profile.presentation.request;

import jakarta.validation.constraints.Size;

public record ProfileValueTalkPair(Long profileValueTalkId, @Size(max = 300) String answer,
                                   String summary) {

}
