package OnlineBookingSystem.OnlineBookingSystem.service.Impl;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.PaymentHistoryResponse;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.TODO;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImplementation implements PaymentService {

	@Autowired
	private APIContext apiContext;

	@Autowired
	PaymentRepository paymentRepository;

	@Value("${stripe.api.key}")
	private String stripeApiKey;

	@Value("${paystack.secret.key}")
	private String secretKey;

	@Value("${paystack.base.url}")
	private String baseUrl;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final BookingRepository bookingRepository;





	public String paymentProcessings(Double totalFare,String email, PaymentMethod paymentMethod) throws IOException {
		String approvalUrl = null;

		switch (paymentMethod) {
			case PAYPAL:
				approvalUrl = processPaypalPayment(totalFare);
				break;
			case STRIPE:
				approvalUrl = processStripePayment(totalFare);
				break;
			case PAYSTACK:
				approvalUrl = processPaystackPayment(email,totalFare);
				break;

			default:
				throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
		}
		return approvalUrl;
	}


		//PAYPAL PAYMENT INTEGRATION
	private String processPaypalPayment(Double totalFare) {
		String approvalUrl;
		try {
			// Prepare the payment request
			PaymentRequest paymentRequest = new PaymentRequest();
			paymentRequest.setCurrency("EUR");
			paymentRequest.setTotal(totalFare);
			paymentRequest.setPaymentMethod(PaymentMethod.PAYPAL);
			paymentRequest.setDescription("Train ticket booking");
			paymentRequest.setIntent("sale");
			paymentRequest.setCancelUrl("http://localhost:8080/api/payments/pay/cancel");
			paymentRequest.setSuccessUrl("http://localhost:8080/api/payments/pay/success");

			Payment payment = createPaypalPayment(paymentRequest);


			approvalUrl = payment.getLinks().stream()
					.filter(link -> "approval_url".equals(link.getRel()))
					.findFirst()
					.map(com.paypal.api.payments.Links::getHref)
					.orElseThrow(() -> new RuntimeException("Approval URL not found in the payment response."));
		} catch (PayPalRESTException e) {
			throw new RuntimeException("Payment failed: " + e.getMessage());
		}

		if (approvalUrl == null) {
			throw new RuntimeException("Approval URL not found. Payment creation might have failed.");
		}
		return approvalUrl;
	}


	private Payment createPaypalPayment(PaymentRequest paymentRequest) throws PayPalRESTException {
		Amount amount = new Amount();
		amount.setCurrency(paymentRequest.getCurrency());
		amount.setTotal(String.valueOf(paymentRequest.getTotal()));

		Transaction transaction = new Transaction();
		transaction.setDescription(paymentRequest.getDescription());
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(paymentRequest.getPaymentMethod().toString());

		Payment newPayment = new Payment();
		newPayment.setIntent(paymentRequest.getIntent());
		newPayment.setPayer(payer);
		newPayment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(paymentRequest.getCancelUrl());
		redirectUrls.setReturnUrl(paymentRequest.getSuccessUrl());
		newPayment.setRedirectUrls(redirectUrls);
		return newPayment.create(apiContext);
	}




	public Payment executePaypalPayment(String paymentId, String payerId) throws PayPalRESTException {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		return payment.execute(apiContext, paymentExecution);
	}


	//PAYSTACK PAYMENT INTEGRATION
	public String processPaystackPayment(String email, Double amount) throws IOException {
		String url = baseUrl + "/transaction/initialize";

		// Create Request Payload
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", email);
		payload.put("amount", amount * 100);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost request = new HttpPost(url);
			request.setHeader("Authorization", "Bearer " + secretKey);
			request.setHeader("Content-Type", "application/json");

			StringEntity entity = new StringEntity(objectMapper.writeValueAsString(payload));
			request.setEntity(entity);

			try (CloseableHttpResponse response = client.execute(request)) {
				if (response.getCode() == HttpStatus.OK.value()) {
					return new String(response.getEntity().getContent().readAllBytes());
				} else {
					return "Error: " + response.getCode();
				}
			}
		}
	}

public String verifyPaystackPaymentTransaction(String reference) throws IOException {
	String url = baseUrl + "/transaction/verify/" + reference;
	log.info("Verifying Paystack payment with reference: {}", reference);

	try (CloseableHttpClient client = HttpClients.createDefault()) {
		HttpGet request = new HttpGet(url);
		request.setHeader("Authorization", "Bearer " + secretKey);
		request.setHeader("Content-Type", "application/json");

		try (CloseableHttpResponse response = client.execute(request)) {
			String jsonResponse = new String(response.getEntity().getContent().readAllBytes());
			log.info("Paystack verification response: {}", jsonResponse);

			if (response.getCode() == HttpStatus.OK.value()) {
				JsonNode jsonNode = objectMapper.readTree(jsonResponse);
				String status = jsonNode.get("data").get("status").asText();

				if ("success".equalsIgnoreCase(status)) {
					String email = jsonNode.get("data").get("customer").get("email").asText();

					Booking booking = bookingRepository.findByUser_Email(email);
					if (booking != null) {
						booking.getBookingPayment().setPaymentStatus(PaymentStatus.COMPLETED);
//						booking.setPaymentStatus(PaymentStatus.COMPLETED);
						bookingRepository.save(booking);
						log.info("Booking updated to PAID for user: {}", email);
					}
					return "Payment Successful";
				} else {
					return "Payment Verification Failed: Payment was not successful";
				}
			} else {
				return "Error: Unable to verify payment, response code " + response.getCode();
			}
		}
	}
}




//STRIPE  PAYMENT INTEGRATION

	public String processStripePayment(Double totalFare) {
		try {
			Stripe.apiKey = stripeApiKey;
			PaymentIntentCreateParams params =
					PaymentIntentCreateParams.builder()
							.setAmount((long) (totalFare * 100))
							.setCurrency("usd")
							.setDescription("Train ticket booking")
							.build();
			PaymentIntent intent = PaymentIntent.create(params);
			return intent.getClientSecret();
		} catch (StripeException e) {
			throw new RuntimeException("Stripe payment failed: " + e.getMessage());
		}
	}









}