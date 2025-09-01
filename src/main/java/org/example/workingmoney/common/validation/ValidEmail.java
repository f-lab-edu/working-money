package org.example.workingmoney.common.validation;

import static java.lang.annotation.ElementType.FIELD;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank
@Email
@ReportAsSingleViolation
public @interface ValidEmail {
    String message() default "유효하지 않은 이메일입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}