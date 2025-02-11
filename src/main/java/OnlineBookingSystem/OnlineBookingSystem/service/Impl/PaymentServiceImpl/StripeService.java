package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import com.stripe.exception.StripeException;
import org.springframework.http.ResponseEntity;

public interface StripeService {
    String processStripePayment(Double totalFare, Long userId, Long bookingId);

    void confirmPayment(String id) throws StripeException;
}
