package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;

/**
 * Validator for inbound receipt line type.
 */
public class InboundReceiptLineTypeValidator implements ConstraintValidator<
    CorrectInboundReceiptLineType, String> {

  private static final String LINE_TYPE_ITEM = "ITEM";
  private static final String LINE_TYPE_SET = "SET";

  @Override
  public void initialize(CorrectInboundReceiptLineType constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String lineType, jakarta.validation.ConstraintValidatorContext
      constraintValidatorContext) {
    return lineType.equals(LINE_TYPE_ITEM) || lineType.equals(LINE_TYPE_SET);
  }
}
