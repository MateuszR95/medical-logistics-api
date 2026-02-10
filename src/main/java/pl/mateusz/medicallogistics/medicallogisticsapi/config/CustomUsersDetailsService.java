package pl.mateusz.medicallogistics.medicallogisticsapi.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.repository.UserRepository;

/**
 * Custom implementation of UserDetailsService for loading user-specific data.
 */
@Service
public class CustomUsersDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Constructs a CustomUsersDetailsService with the specified UserRepository.
   *
   * @param userRepository the repository for accessing user data
   */
  public CustomUsersDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return createUserDetails(username.toLowerCase());
  }

  private UserDetails createUserDetails(String email) {

    pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User appUser
        = userRepository.findByEmailAndActiveTrue(email).orElseThrow(() ->
        new UsernameNotFoundException(String.format("User with email %s"
        + " not found or not active", email)));

    return User.builder()
      .username(appUser.getEmail())
      .password(appUser.getPasswordHash())
      .roles(appUser.getRole().toString())
      .disabled(!appUser.isActive())
      .build();

  }
}
