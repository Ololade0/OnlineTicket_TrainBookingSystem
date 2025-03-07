package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {
    String processPaypalPayment(Double totalFare, User user, Booking booking);

    Payment executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException;
}
