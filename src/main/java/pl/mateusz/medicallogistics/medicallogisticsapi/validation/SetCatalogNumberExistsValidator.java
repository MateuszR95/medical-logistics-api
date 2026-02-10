package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;
import org.springframework.stereotype.Component;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetBaseRepository;

/**
 * Validator for checking if a set with the given catalog number exists.
 */
@Component
public class SetCatalogNumberExistsValidator implements ConstraintValidator
    <SetCatalogNumberExists, String> {

  private final SetBaseRepository setBaseRepository;

  /**
   * Constructor for SetCatalogNumberExistsValidator.
   *
   * @param setBaseRepository the repository to check for the existence of the set catalog number
   */
  public SetCatalogNumberExistsValidator(SetBaseRepository setBaseRepository) {
    this.setBaseRepository = setBaseRepository;
  }

  @Override
  public void initialize(SetCatalogNumberExists constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String setCatalogNumber,
                         jakarta.validation.ConstraintValidatorContext constraintValidatorContext) {
    if (setCatalogNumber == null || setCatalogNumber.isBlank()) {
      return true;
    }
    return setBaseRepository.existsByCatalogNumber(setCatalogNumber.trim());
  }
}
