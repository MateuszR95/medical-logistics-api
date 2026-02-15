package pl.mateusz.medicallogistics.medicallogisticsapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.domain.InboundReceiptLine;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto.InboundReceiptLineDto;
import pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.repository.InboundReceiptLineRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.lot.repository.LotRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.set.repository.SetInstanceRepository;

/**
 * Validator for InboundReceiptLineDto to enforce custom validation rules.
 */
@Component
public class InboundReceiptLineValidator implements ConstraintValidator<
    ValidInboundReceiptLine, InboundReceiptLineDto> {
  
  private final LotRepository lotRepository;

  private final SetInstanceRepository setInstanceRepository;

  private final InboundReceiptLineRepository inboundReceiptLineRepository;

  private static final String SET_LINE_TYPE = "SET";


  /**
   * Constructor for InboundReceiptLineValidator.
   *
   * @param lotRepository Repository for accessing lot data.
   * @param setInstanceRepository Repository for accessing set instance data.
   * @param inboundReceiptLineRepository Repository for accessing inbound receipt line data.
   */
  public InboundReceiptLineValidator(LotRepository lotRepository,
                                     SetInstanceRepository setInstanceRepository,
                                     InboundReceiptLineRepository inboundReceiptLineRepository) {
    this.lotRepository = lotRepository;
    this.setInstanceRepository = setInstanceRepository;
    this.inboundReceiptLineRepository = inboundReceiptLineRepository;
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
    if (SET_LINE_TYPE.equalsIgnoreCase(dto.getLineType())) {
      isValid &= validateUniqueSetTagIdNumber(dto, ctx);
    }
    return isValid;
  }

  private boolean validateUniqueSetTagIdNumber(InboundReceiptLineDto dto,
                                                      ConstraintValidatorContext ctx) {
    Long lineBatchId = dto.getBatchId();
    String lineSetTagId = dto.getSetTagId();
    boolean existsByTagId = setInstanceRepository.existsByTagId(dto.getSetTagId());
    if (existsByTagId) {
      Optional<InboundReceiptLine> receiptLineDtoOptional = inboundReceiptLineRepository
          .findFirstByBatchIdAndSetTagId(lineBatchId, lineSetTagId);
      if (receiptLineDtoOptional.isEmpty()) {
        addFieldViolation(ctx, "setTagId",
            "This set tag ID is already assigned to a different batch.");
        return false;
      } else {
        return true;
      }
    }
    return true;
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
    if (!"SET".equalsIgnoreCase(dto.getLineType())) {
      return true;
    }
    boolean isValid = true;
    String setCatalogNumber = dto.getSetCatalogNumber();
    if (setCatalogNumber == null || setCatalogNumber.isBlank()) {
      addFieldViolation(ctx, "setCatalogNumber",
           "setCatalogNumber is required when lineType is SET.");
      isValid = false;
    }
    String setTagId = dto.getSetTagId();
    if (setTagId == null || setTagId.isBlank()) {
      addFieldViolation(ctx, "setTagId",
          "setTagId is required when lineType is SET.");
      isValid = false;
    }
    return isValid;
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
