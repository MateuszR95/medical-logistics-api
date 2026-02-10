package pl.mateusz.medicallogistics.medicallogisticsapi.inbound.receipt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for inbound receipt batch information.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundReceiptBatchDto {

  @NotBlank(message = "Batch number cannot be blank")
  private Long id;

  @NotBlank(message = "Batch number cannot be blank")
  @Size(max = 40)
  private String batchNumber;

  @NotBlank(message = "File name cannot be blank")
  @Size(max = 200)
  private String fileName;

  @Size(max = 30)
  private String sourceWarehouseCode;

  @NotNull(message = "Received to warehouse id cannot be null")
  private Long receivedToWarehouseId;

  @NotNull(message = "Status cannot be null")
  private String status;

  @NotNull(message = "Created by id cannot be null")
  private Long createdById;

  @Size(max = 800)
  private String comment;

  @NotNull
  private LocalDateTime createdAt;

}
