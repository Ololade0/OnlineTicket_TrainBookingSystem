package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findBySeatNumber(int seatNumber);
// List<Integer> findSeatNumberByTrainClass(TrainClass trainClass);
}
