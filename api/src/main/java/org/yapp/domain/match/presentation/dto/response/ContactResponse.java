package org.yapp.domain.match.presentation.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.yapp.core.domain.profile.ContactType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ContactResponse {

    private Map<ContactType, String> contacts;
}