package OnlineBookingSystem.OnlineBookingSystem.service.Impl;//
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayPalServiceImpl;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayStackService;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.StripeService;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImplementation implements PaymentService {
	private final PayPalServiceImpl payPalService;
	private final StripeService stripeService;
	private final PayStackService payStackService;

	private final PaymentRepository paymentRepository;

	public String paymentProcessings(Double totalFare, User user, Booking booking, String email, PaymentMethod paymentMethod) throws IOException, InterruptedException {

		return switch (paymentMethod) {
			case paypal ->  payPalService.processPaypalPayment(totalFare, user, booking);
			case STRIPE -> stripeService.processStripePayment(totalFare, user, booking);
			case PAYSTACK -> payStackService.processPaystackPayment(user.getEmail(), totalFare);
			default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
		};
	}

	public boolean verifyPayment(Booking booking) {
		String paymentStatus = checkPaymentStatusFromGateway(booking.getApprovalUrl());
		return "SUCCESS".equalsIgnoreCase(paymentStatus);
	}

	@Override
	public Optional<BookingPayment> findBytransactionReference(String paymentIntentId) {
		return Optional.ofNullable(paymentRepository.findBytransactionReference(paymentIntentId));

	}

	@Override
	public BookingPayment savePaymentInfo(BookingPayment bookingPayment) {
		return paymentRepository.save(bookingPayment);
	}

	private String checkPaymentStatusFromGateway(String approvalUrl) {
		return "SUCCESS";
	}

}