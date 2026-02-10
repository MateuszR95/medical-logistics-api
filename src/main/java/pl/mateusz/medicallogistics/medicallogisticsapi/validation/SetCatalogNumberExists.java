package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating catalog number existence.
 */
@Documented
@Constraint(validatedBy = SetCatalogNumberExistsValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface SetCatalogNumberExists {

  /**
   * The default message for the item reference number existence validation.
   *
   * @return the default message
   */
  String message() default "{pl.mateusz.SetCatalogNumberExists.message}";

  /**
   * The groups the constraint belongs to.
   *
   * @return the groups
   */

  Class<?>[] groups() default {};

  /**
   * The payload associated with the constraint.
   *
   * @return the payload
   */
  Class<? extends Payload>[] payload() default {};


}
