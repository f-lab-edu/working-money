package org.example.workingmoney.common.validation;

import static java.lang.annotation.ElementType.FIELD;

import jakarta.validation.Constraint;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(FIELD)@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank
@Size(min = 2, max = 10)
@Pattern(regexp = "^[A-Za-z0-9가-힣]+$")
@ReportAsSingleViolation
public @interface ValidNickname {
    String message() default "유효하지 않은 닉네임입니다.";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
