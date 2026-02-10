package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom annotation for validating inbound receipt line type.
 */
@Documented
@Constraint(validatedBy = InboundReceiptLineTypeValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface CorrectInboundReceiptLineType {

  /**
   * The default message for the inbound receipt line type validation.
   *
   * @return the default message
   */
  String message() default "{pl.mateusz.InboundReceiptLineType.message}";

  /**
   * The groups the constraint belongs to.
   *
   * @return the groups
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<?>[] groups() default {};

  /**
   * The payload associated with the constraint.
   *
   * @return the payload
   */
  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  Class<? extends Payload>[] payload() default {};
}
