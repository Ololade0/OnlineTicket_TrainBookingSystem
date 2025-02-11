package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.BookingService;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;

import OnlineBookingSystem.OnlineBookingSystem.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final BookingRepository bookingRepository;
    private final SeatService seatService;
    private final UserService userService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }



    public String processStripePayment(Double totalFare, Long userId, Long bookingId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (user.isEmpty() || booking.isEmpty()) {
            throw new RuntimeException("User or Booking not found");
        }

        try {
            Stripe.apiKey = stripeApiKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (totalFare * 100))
                    .setCurrency("usd")
                    .setDescription("Train ticket booking")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            BookingPayment bookingPayment = BookingPayment.builder()
                    .paymentMethod(PaymentMethod.STRIPE)
                    .paymentStatus(PaymentStatus.PENDING)
                    .paymentDate(LocalDateTime.now())
                    .booking(booking.get())
                    .user(user.get())
                    .totalPrice(totalFare)
                    .transactionReference(intent.getId())
                    .successUrl(intent.getClientSecret())
                    .build();

            paymentRepository.save(bookingPayment);

            return intent.getClientSecret();

        } catch (StripeException e) {
            throw new RuntimeException("Stripe payment failed: " + e.getMessage());
        }

}

    @Transactional
    public void confirmPayment(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        if ("succeeded".equals(paymentIntent.getStatus())) {
            Optional<BookingPayment> optionalPayment = Optional.ofNullable(paymentRepository.findBytransactionReference(paymentIntentId));

            if (optionalPayment.isPresent()) {
                BookingPayment bookingPayment = optionalPayment.get();
                bookingPayment.setPaymentStatus(PaymentStatus.COMPLETED);
                bookingPayment.setPaymentDate(LocalDateTime.now());
                paymentRepository.save(bookingPayment);


                Booking booking = bookingPayment.getBooking();
                booking.setBookingStatus(BookingStatus.BOOKED);
                bookingRepository.save(booking);

                // Book the seat
                Seat bookedSeat = seatService.bookSeat(booking.getTrainClass().getClassName(), booking.getSeatNumber());
                bookedSeat.setStatus(SeatStatus.BOOKED);
                seatService.updateSeat(bookedSeat);
            }
        }
    }
}
