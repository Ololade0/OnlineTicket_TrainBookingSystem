package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
}
