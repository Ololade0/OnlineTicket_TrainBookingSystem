package OnlineBookingSystem.OnlineBookingSystem.controller;


import OnlineBookingSystem.OnlineBookingSystem.dto.response.PaymentHistoryResponse;

import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
    private PaymentService paymentService;

	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";

	@GetMapping("/")
	public String home() {
		return "home";
	}


//	@GetMapping(value = CANCEL_URL)
//	public String cancelPay() {
//		return "cancel";
//	}
@GetMapping(value = CANCEL_URL)
public ResponseEntity<String> cancelPay() {
	// Generate a cancellation response
	String responseHtml = generateCancelResponse();
	return ResponseEntity.ok()
			.contentType(MediaType.TEXT_HTML)
			.body(responseHtml);
}

	private String generateCancelResponse() {
		return "<html><body><h1>Payment Canceled</h1>" +
				"<p>Your payment has been canceled successfully.</p>" +
				"<p>If you have any questions, please contact support.</p>" +
				"<a href=\"/\">Return to Home</a>" +
				"</body></html>";
	}
		@GetMapping(value = SUCCESS_URL)
	public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
											 @RequestParam("PayerID") String payerId) {
		try {
			// Execute the payment
			Payment payment = paymentService.executePayment(paymentId, payerId);
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

	private String generateSuccessResponse(String paymentId, String payerId) {
		return "<html><body><h1>Payment Successful!</h1>" +
				"<p>Payment ID: " + paymentId + "</p>" +
				"<p>Payer ID: " + payerId + "</p></body></html>";
	}



	@GetMapping("/history")
	public ResponseEntity<?> getPaymentHistory(@RequestParam Long userId) {
		List<PaymentHistoryResponse> paymentHistory = paymentService.getPaymentHistory(userId);
		return ResponseEntity.ok(paymentHistory);
	}

}
