package uz.uolimov.abs.validation;

import jakarta.validation.Constraint;
import uz.uolimov.abs.model.dto.user.CreateUserRequestDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Constraint(validatedBy = CreateUserRequestValidator.class)
public @interface CreateUserRequest {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends CreateUserRequestDTO>[] payload() default {};
}
