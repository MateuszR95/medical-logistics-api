package pl.mateusz.medicallogistics.medicallogisticsapi.set.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Data transfer object for set instance information.
 * This DTO is used to transfer data related to set instances between
 * different layers of the application,
 * such as from the service layer to the controller layer or for API responses.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetInstanceDto {

  private String setTagId;
  private String setType;
  private Long setBaseId;
  private Long locationId;
  private String setStatus;
  private boolean isActive;


}
