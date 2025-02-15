package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<BookingPayment, Long> {
//    List<BookingPayment> findByUserId(String userId);

    List<BookingPayment> findByBooking_User_Id(Long userId);
    @Query("SELECT b FROM payments b WHERE b.transactionReference = ?1")
    BookingPayment findBytransactionReference(String transactionReference);

}
