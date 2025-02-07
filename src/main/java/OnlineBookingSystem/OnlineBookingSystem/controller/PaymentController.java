package OnlineBookingSystem.OnlineBookingSystem.controller;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayPalService;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayStackService;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.StripeService;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
    private PayPalService payPalService;

	@Autowired
	private PayStackService payStackService;

	@Autowired
	private StripeService stripeService;


	@Value("${stripe.api.key}")
	private String stripeApiKey;


	@GetMapping("/")
	public String home() {
		return "home";
	}

@GetMapping("/pay/cancel")
public ResponseEntity<String> cancelPaymentTransaction() {
	String responseHtml = generateCancelResponse();
	return ResponseEntity.ok()
			.contentType(MediaType.TEXT_HTML)
			.body(responseHtml);
}


		@GetMapping("pay/success")
	public ResponseEntity<String> successPayForPayPal(@RequestParam("paymentId") String paymentId,
											 @RequestParam("PayerID") String payerId) {
		try {
			// Execute the payment
			Payment payment = payPalService.executePaypalPayment(paymentId, payerId);
			System.out.println(payment.toJSON());

			if ("approved".equals(payment.getState())) {
				return ResponseEntity.ok()
						.contentType(MediaType.TEXT_HTML)
						.body(generateSuccessResponse(paymentId, payerId));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Payment was not approved.");
			}
		} catch (PayPalRESTException e) {
			System.err.println("Error during payment execution: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the payment.");
		}
	}





	@GetMapping("/create-payment-intent")
	public String createPaymentIntentForStripe(@RequestParam Double totalFare, Model model) {
		System.out.println("Total Fare: " + totalFare); // Debug statement
		String clientSecret = stripeService.processStripePayment(totalFare);
		model.addAttribute("clientSecret", clientSecret);
		return "checkout";
	}

	@PostMapping("/pay")
	public ResponseEntity<?> processPaymentForPayStack(@RequestBody PaymentRequest request) {
		try {
			String response = payStackService.processPaystackPayment(request.getEmail(), request.getTotal());
			return ResponseEntity.ok(response);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Payment Initialization Failed");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/verify/{reference}")
	public ResponseEntity<?> verifyPaymentForPayStack(@PathVariable String reference) {
		try {
			String response = payStackService.verifyPaystackPaymentTransaction(reference);
			return ResponseEntity.ok(response);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Payment Verification Failed: " + e.getMessage());
		}
	}

	@PostMapping("/paystack/webhook")
	public ResponseEntity<String> handleWebhookForPayStack(@RequestBody Map<String, Object> payload) {
		System.out.println("Received Webhook: " + payload);
		return ResponseEntity.ok("Webhook received");
	}


	private String generateCancelResponse() {
		return "<html><body><h1>Payment Canceled</h1>" +
				"<p>Your payment has been canceled successfully.</p>" +
				"<p>If you have any questions, please contact support.</p>" +
				"<a href=\"/\">Return to Home</a>" +
				"</body></html>";
	}


	private String generateSuccessResponse(String paymentId, String payerId) {
		return "<html><body><h1>Payment Successful!</h1>" +
				"<p>Payment ID: " + paymentId + "</p>" +
				"<p>Payer ID: " + payerId + "</p></body></html>";
	}



}

