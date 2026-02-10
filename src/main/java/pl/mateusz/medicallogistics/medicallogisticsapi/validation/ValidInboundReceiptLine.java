package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating inbound receipt line.
 */
@Documented
@Constraint(validatedBy = InboundReceiptLineValidator.class)
@Target({ ElementType.TYPE })
@Retention(RUNTIME)
public @interface ValidInboundReceiptLine {

  /**
   * The default message for the inbound receipt line validation.
   *
   * @return the default message
   */
  String message() default "{pl.mateusz.ValidInboundReceiptLine.message}";

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
