package org.example.workingmoney.common.validation;

import static java.lang.annotation.ElementType.FIELD;

import jakarta.validation.Constraint;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.example.workingmoney.common.constants.PostConstants;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotBlank
@Size(min = PostConstants.MIN_POST_CONTENT_LENGTH, max = PostConstants.MAX_POST_CONTENT_LENGTH)
@ReportAsSingleViolation
public @interface ValidPostContent {

    String message() default "제목은 " + PostConstants.MIN_POST_CONTENT_LENGTH + "자 이상, "
            + PostConstants.MAX_POST_CONTENT_LENGTH + "자 이하이어야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends jakarta.validation.Payload>[] payload() default {};
}
