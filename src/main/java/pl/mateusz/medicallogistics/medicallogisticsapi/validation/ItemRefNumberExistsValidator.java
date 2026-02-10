package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.repository.ItemRepository;

/**
 * Validator for checking if an item with the given reference number exists.
 */

@Component
public class ItemRefNumberExistsValidator implements ConstraintValidator
    <ItemRefNumberExists, String> {

  private final ItemRepository itemRepository;


  /**
   * Constructor for ItemRefNumberExistsValidator.
   *
   * @param itemRepository the repository to check for item existence
   */
  public ItemRefNumberExistsValidator(ItemRepository itemRepository) {
    this.itemRepository = itemRepository;
  }

  @Override
  public void initialize(ItemRefNumberExists constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String itemRefNumber,
                         ConstraintValidatorContext constraintValidatorContext) {
    return itemRepository.existsByRefNumber(itemRefNumber);
  }
}
