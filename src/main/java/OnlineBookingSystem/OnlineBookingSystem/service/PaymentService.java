package OnlineBookingSystem.OnlineBookingSystem.service;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.PaymentHistoryResponse;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.io.IOException;
import java.util.List;

public interface PaymentService {
    String paymentProcessings(Double totalFare, User user, Booking booking, String email, PaymentMethod paymentMethod) throws IOException, InterruptedException;

    boolean verifyPayment(Booking primaryBooking);
}
