package OnlineBookingSystem.OnlineBookingSystem.service.Impl;//
//
//package OnlineBookingSystem.OnlineBookingSystem.service.Impl;
//
//import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
//import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
//import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
//import OnlineBookingSystem.OnlineBookingSystem.model.User;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.Currency;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
//import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
//import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
//import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
//import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
//import ch.qos.logback.core.testUtil.RandomUtil;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.PaymentIntent;
//import com.stripe.param.PaymentIntentCreateParams;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.hc.client5.http.classic.methods.HttpGet;
//import org.apache.hc.client5.http.classic.methods.HttpPost;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
//import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.core5.http.io.entity.StringEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.LocalDateTime;
//import java.util.*;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayPalServiceImpl;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayStackService;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.StripeService;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImplementation implements PaymentService {
	private final PayPalServiceImpl payPalService;
	private final StripeService stripeService;
	private final PayStackService payStackService;

	public String paymentProcessings(Double totalFare, User user, Booking booking, String email, PaymentMethod paymentMethod) throws IOException, InterruptedException {

		return switch (paymentMethod) {
			case paypal ->  payPalService.processPaypalPayment(totalFare, user, booking);
			case STRIPE -> stripeService.processStripePayment(totalFare);
			case PAYSTACK -> payStackService.processPaystackPayment(user.getEmail(), totalFare);
			default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
		};
	}

	public boolean verifyPayment(Booking booking) {
		// Simulating a payment verification process
		// Ideally, this should make an API call to the payment gateway to check the status
		String paymentStatus = checkPaymentStatusFromGateway(booking.getApprovalUrl());

		return "SUCCESS".equalsIgnoreCase(paymentStatus);
	}

	private String checkPaymentStatusFromGateway(String approvalUrl) {
		// Simulated response from a payment gateway API
		// This should be an actual HTTP request to the gateway's verification endpoint
		return "SUCCESS"; // Change this to simulate different responses
	}

}