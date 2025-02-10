package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;
    private final PaymentRepository paymentRepository;
    private BookingRepository bookingRepository;

    private final SeatService seatService;


    public String processStripePayment(Double totalFare, User user, Booking booking) {
        try {
            Stripe.apiKey = stripeApiKey;

            // Create a PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (totalFare * 100))
                    .setCurrency("usd")
                    .setDescription("Train ticket booking")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // Save initial payment record with PENDING status
            BookingPayment bookingPayment = BookingPayment.builder()
                    .paymentMethod(PaymentMethod.STRIPE)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentDate(LocalDateTime.now())
                    .booking(booking)
                    .user(user)
                    .totalPrice(totalFare)
                    .transactionReference(intent.getId())
                    .successUrl(intent.getClientSecret())
                    .build();

//           paymentService.savePaymentInfo(bookingPayment);
            paymentRepository.save(bookingPayment);

            return intent.getClientSecret(); // Send client secret to frontend for payment confirmation

        } catch (StripeException e) {
            throw new RuntimeException("Stripe payment failed: " + e.getMessage());
        }
    }

    @Transactional
    public void confirmPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            if ("succeeded".equals(paymentIntent.getStatus())) {
                BookingPayment paymentOptional = paymentRepository.findBytransactionReference(paymentIntentId);

                if (paymentOptional != null) {
                    BookingPayment bookingPayment = paymentOptional;
                    bookingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
                    bookingPayment.setPaymentDate(LocalDateTime.now());
                    paymentRepository.save(bookingPayment);

                    // Update booking status to "BOOKED"
                    Booking booking = bookingPayment.getBooking();
                    booking.setBookingStatus(BookingStatus.BOOKED);

                    // **BOOK SEAT HERE**
                    try {
                        Seat bookedSeat = seatService.bookSeat(booking.getTrainClass().getClassName(), booking.getSeatNumber());
                        // You might want to associate the bookedSeat with the booking entity

                    } catch (Exception e) {
                        // Handle seat booking failure appropriately (e.g., refund, notify user)
                        throw new RuntimeException("Error booking seat: " + e.getMessage());
                    }

                    bookingRepository.save(booking);
                }
            }
        } catch (StripeException e) {
            throw new RuntimeException("Error confirming payment: " + e.getMessage());
        }
    }
}





