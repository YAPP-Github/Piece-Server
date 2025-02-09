package org.yapp.domain.profile.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.yapp.core.domain.profile.ContactType;

public class ContactTypeValidator implements
    ConstraintValidator<ValidContactType, Map<String, String>> {

    private static final Set<String> VALID_CONTACT_TYPES = Arrays.stream(ContactType.values())
        .map(Enum::name)
        .collect(Collectors.toSet());

    @Override
    public boolean isValid(Map<String, String> contacts, ConstraintValidatorContext context) {
        if (contacts == null || contacts.isEmpty() || !contacts.containsKey(
            ContactType.KAKAO_TALK_ID.name())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("카카오톡 ID는 필수 값입니다.")
                .addConstraintViolation();
            return false;
        }

        for (String key : contacts.keySet()) {
            if (!VALID_CONTACT_TYPES.contains(key)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("지원하지 않는 연락처 타입: " + key)
                    .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}