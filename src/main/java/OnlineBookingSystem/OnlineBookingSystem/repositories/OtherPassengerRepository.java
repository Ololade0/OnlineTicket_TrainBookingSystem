package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtherPassengerRepository extends JpaRepository<OtherPassenger, Long> {
    Optional<OtherPassenger> findByEmail(String email);

    List<OtherPassenger> findByBooking(Booking primaryBooking);
}
