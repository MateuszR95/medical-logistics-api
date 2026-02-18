package pl.mateusz.medicallogistics.medicallogisticsapi.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.inspection.discrepancy.line.SetInspectionDiscrepancyType;


/**
 * Validator for the @ValidSetInspectionDiscrepancyType annotation.
 * Validates that the provided string value corresponds to a valid SetInspectionDiscrepancyType
 * enum constant.
 */
public class SetInspectionDiscrepancyLineTypeValidator implements ConstraintValidator
    <ValidSetInspectionDiscrepancyType, String> {

  @Override
  public void initialize(ValidSetInspectionDiscrepancyType constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String discrepancyType,
                         ConstraintValidatorContext constraintValidatorContext) {
    List<SetInspectionDiscrepancyType> setInspectionDiscrepancyTypes = Arrays
        .stream(SetInspectionDiscrepancyType
        .values()).toList();
    return setInspectionDiscrepancyTypes.stream().anyMatch(type ->
        type.name().equals(discrepancyType));
  }
}
