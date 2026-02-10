package pl.mateusz.medicallogistics.medicallogisticsapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user credentials information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentialsInfoDto {

  private Long id;
  private String email;
  private String role;
  private boolean active;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
