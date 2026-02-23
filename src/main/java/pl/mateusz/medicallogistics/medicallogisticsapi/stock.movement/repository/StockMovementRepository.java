package pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.mateusz.medicallogistics.medicallogisticsapi.stock.movement.domain.StockMovement;

/**
 * Repository interface for managing StockMovement entities.
 * Extends JpaRepository to provide CRUD operations and database interaction
 * for the StockMovement entity.
 */
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
