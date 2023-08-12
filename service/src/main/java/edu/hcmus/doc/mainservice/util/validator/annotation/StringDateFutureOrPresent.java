package edu.hcmus.doc.mainservice.util.validator.annotation;

import edu.hcmus.doc.mainservice.util.validator.StringDateValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringDateValidator.class)
public @interface StringDateFutureOrPresent {

  String message() default "Date must be in the future or present";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
