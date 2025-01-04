package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatNumber(int seatNumber);

    @Query("SELECT s FROM seats s WHERE s.seatNumber = :seatNumber AND s.trainClass.className = :trainClassName")
    Optional<Seat> findBySeatNumberAndTrainClass_ClassName(@Param("trainClassName") String trainClassName, @Param("seatNumber") int seatNumber);


}
