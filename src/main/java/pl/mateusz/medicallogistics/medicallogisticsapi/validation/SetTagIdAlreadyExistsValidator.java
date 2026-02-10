package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;

/**
 * Validator to check if a Set Tag ID already exists.
 */
@Component
public class SetTagIdAlreadyExistsValidator implements ConstraintValidator
    <SetTagIdAlreadyExists, String> {

  private final SetInstanceRepository setInstanceRepository;

  /**
   * Constructor for SetTagIdAlreadyExistsValidator.
   *
   * @param setInstanceRepository the repository to check for existing Set Tag IDs
   */
  public SetTagIdAlreadyExistsValidator(SetInstanceRepository setInstanceRepository) {
    this.setInstanceRepository = setInstanceRepository;
  }

  @Override
  public void initialize(SetTagIdAlreadyExists constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String setTagId, ConstraintValidatorContext constraintValidatorContext) {
    if (setTagId == null || setTagId.isBlank()) {
      return true;
    }
    return !setInstanceRepository.existsByTagId(setTagId.trim());
  }
}
