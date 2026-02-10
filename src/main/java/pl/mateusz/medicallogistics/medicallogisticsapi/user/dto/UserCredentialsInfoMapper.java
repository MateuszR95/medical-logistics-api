package pl.mateusz.medicallogistics.medicallogisticsapi.user.dto;

import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;

/**
 * Mapper class for converting User entities to UserCredentialsInfoDto.
 */
public class UserCredentialsInfoMapper {

  /**
   * Converts a User entity to a UserCredentialsInfoDto.
   *
   * @param user the User entity to convert
   * @return a UserCredentialsInfoDto containing the user's credentials information
   */
  public static UserCredentialsInfoDto fromEntity(User user) {
    return UserCredentialsInfoDto.builder()
      .id(user.getId())
      .email(user.getEmail())
      .role(user.getRole().name())
      .active(user.isActive())
      .build();
  }
}
