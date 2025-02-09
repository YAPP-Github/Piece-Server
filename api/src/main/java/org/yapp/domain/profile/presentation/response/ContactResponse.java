package org.yapp.domain.profile.presentation.response;

import org.yapp.core.domain.profile.ContactType;

public record ContactResponse(ContactType type, String value) {

}
