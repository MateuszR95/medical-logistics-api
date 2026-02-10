package pl.mateusz.medicallogistics.medicallogisticsapi.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.user.domain.User;


/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds a user by their email address and checks if the account is active.
   *
   * @param email the email address of the user
   * @return an Optional containing the User if found and active, or empty if not found or inactive
   */
  Optional<User> findByEmailAndActiveTrue(String email);
}
