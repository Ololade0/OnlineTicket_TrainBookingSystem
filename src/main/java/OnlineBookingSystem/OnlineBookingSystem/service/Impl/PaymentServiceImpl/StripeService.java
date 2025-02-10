package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import org.springframework.http.ResponseEntity;

public interface StripeService {
    String processStripePayment(Double totalFare, User user, Booking booking);

    void confirmPayment(String id);
}
