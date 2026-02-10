package pl.mateusz.medicallogistics.medicallogisticsapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user credentials.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequestDto {

  private String email;
  private String password;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
