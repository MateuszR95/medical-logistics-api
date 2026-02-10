package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.stereotype.Component;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;

/**
 * Validator for InboundReceiptLineDto to enforce custom validation rules.
 */
@Component
public class InboundReceiptLineValidator implements ConstraintValidator<
    ValidInboundReceiptLine, InboundReceiptLineDto> {
  
  private final LotRepository lotRepository;

  /**
   * Constructs an InboundReceiptLineValidator with the specified LotRepository.
   *
   * @param lotRepository the repository for accessing Lot entities
   */
  public InboundReceiptLineValidator(LotRepository lotRepository) {
    this.lotRepository = lotRepository;
  }

  @Override
  public void initialize(ValidInboundReceiptLine constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(InboundReceiptLineDto dto, ConstraintValidatorContext ctx) {
    if (dto == null) {
      return true;
    }
    boolean isValid = true;
    ctx.disableDefaultConstraintViolation();
    isValid &= validateExpirationDateForSterileItem(dto, ctx);
    isValid &= validateSetTagIdForSetTypeLine(dto, ctx);
    isValid &= validateUniqueExpirationDateForLotNumber(dto, ctx);
    isValid &= validateUniqueLotNumberForRefNumber(dto, ctx);
    return isValid;
  }


  private boolean validateUniqueLotNumberForRefNumber(InboundReceiptLineDto dto,
                                                           ConstraintValidatorContext ctx) {
    boolean isValid = lotRepository.findByLotNumber(dto.getLotNumber())
        .map(lot -> Objects.equals(lot.getItem().getRefNumber(), dto.getItemRefNumber()))
        .orElse(true);

    if (!isValid) {
      addFieldViolation(ctx, "itemRefNumber",
          "This lot number is already assigned to a different reference number.");
    }

    return isValid;
  }

  private boolean validateUniqueExpirationDateForLotNumber(InboundReceiptLineDto dto,
                               ConstraintValidatorContext ctx) {
    String dtoExp = (dto.getExpirationDate() == null || dto.getExpirationDate().isBlank())
        ? null : dto.getExpirationDate().trim();
    boolean isValid = lotRepository.findByLotNumber(dto.getLotNumber())
        .map(lot -> lot.getExpirationDate() == null || lot.getExpirationDate()
          .toString().equals(dtoExp))
          .orElse(true);
    if (!isValid) {
      addFieldViolation(ctx, "expirationDate",
           "The expiration date for the lot number does not match the existing lot's"
             + " expiration date.");
    }
    return isValid;
  }

  private static boolean validateSetTagIdForSetTypeLine(InboundReceiptLineDto dto,
                                                        ConstraintValidatorContext ctx) {
    String setCatalogNumber = dto.getSetCatalogNumber();
    if (setCatalogNumber != null && !setCatalogNumber.isBlank()) {
      String setTagId = dto.getSetTagId();
      if (setTagId == null || setTagId.isBlank()) {
        addFieldViolation(ctx, "setTagId",
            "SetTagId is required when SetCatalogNumber is provided.");
        return false;
      }
    }
    return true;
  }

  private static boolean validateExpirationDateForSterileItem(InboundReceiptLineDto dto,
                                                              ConstraintValidatorContext ctx) {
    String itemRef = dto.getItemRefNumber();
    if (itemRef != null && endsWithS(itemRef)) {
      String expirationDate = dto.getExpirationDate();
      if (expirationDate == null || expirationDate.isBlank()) {
        addFieldViolation(ctx, "expirationDate",
             "Expiration date is required for items with itemRefNumber ending with 'S'.");
        return false;
      }
    }
    return true;
  }

  private static void addFieldViolation(ConstraintValidatorContext ctx, String field,
                                        String message) {
    ctx.buildConstraintViolationWithTemplate(message)
      .addPropertyNode(field)
        .addConstraintViolation();
  }

  private static boolean endsWithS(String itemRef) {
    return itemRef.endsWith("S");
  }
}
