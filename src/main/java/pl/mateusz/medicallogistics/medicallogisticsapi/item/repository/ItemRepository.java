package pl.mateusz.medicallogistics.medicallogisticsapi.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.item.domain.Item;

/**
 * Repository interface for managing Item entities.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
