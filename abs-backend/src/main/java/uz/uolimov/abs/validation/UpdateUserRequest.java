package uz.uolimov.abs.validation;

import jakarta.validation.Constraint;
import uz.uolimov.abs.model.dto.user.UpdateUserRequestDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = UpdateUserRequestValidator.class)
public @interface UpdateUserRequest {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends UpdateUserRequestDTO>[] payload() default {};
}
