package org.yapp.domain.profile.presentation.response;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.yapp.core.domain.profile.ContactType;

public record ContactResponses(List<ContactResponse> contacts) {

    public ContactResponses(List<ContactResponse> contacts) {
        this.contacts = sortContacts(contacts);
    }

    public static List<ContactResponse> convert(Map<ContactType, String> contacts) {
        if (contacts == null) {
            return null;
        }

        List<ContactResponse> contactList = contacts.entrySet().stream()
            .map(entry -> new ContactResponse(entry.getKey(), entry.getValue()))
            .toList();

        return sortContacts(contactList);
    }

    public static List<ContactResponse> sortContacts(List<ContactResponse> contacts) {
        return contacts.stream()
            .sorted(Comparator.comparing(ContactResponse::type))
            .toList();
    }
}
