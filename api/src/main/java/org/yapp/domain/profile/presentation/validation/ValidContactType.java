package org.yapp.domain.profile.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ContactTypeValidator.class)
public @interface ValidContactType {

    String message() default "지원하지 않는 연락처 타입이 포함되어 있습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}