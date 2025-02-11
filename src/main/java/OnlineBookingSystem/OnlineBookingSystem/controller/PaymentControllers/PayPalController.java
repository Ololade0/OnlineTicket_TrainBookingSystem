package OnlineBookingSystem.OnlineBookingSystem.controller.PaymentControllers;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.PaymentProcessingException;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/paypal")
@Slf4j
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @GetMapping("/home")
    @ResponseBody
    public String home() {
        return generateHomePage();
    }

    private String generateHomePage() {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Home - Online Booking System</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                "        header { background-color: #4CAF50; color: white; padding: 15px 20px; text-align: center; }" +
                "        nav { margin: 20px 0; }" +
                "        nav a { margin: 0 15px; color: #4CAF50; text-decoration: none; }" +
                "        main { padding: 20px; text-align: center; }" +
                "        footer { background-color: #4CAF50; color: white; text-align: center; padding: 10px; position: relative; bottom: 0; width: 100%; }" +
                "        @media (max-width: 600px) { nav a { display: block; margin: 10px 0; } }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "<header>" +
                "    <h1>Welcome to the Online Booking System</h1>" +
                "</header>" +
                "<nav>" +
                "    <a href=\"/api/paypal/pay\">Pay with PayPal</a>" +
                "    <a href=\"/api/paystack/pay\">Pay with Paystack</a>" +
                "    <a href=\"/api/stripe/pay\">Pay with Stripe</a>" +
                "</nav>" +
                "<main>" +
                "    <h2>Your One-Stop Solution for Booking</h2>" +
                "    <p>Book your tickets easily and securely.</p>" +
                "</main>" +
                "<footer>" +
                "    <p>&copy; 2025 Online Booking System</p>" +
                "</footer>" +
                "</body>" +
                "</html>";
    }
    @GetMapping("/pay/cancel")
    public ResponseEntity<String> cancelPaypalPayment(@RequestParam("transactionReference") String transactionReference) {
        try {
            payPalService.handleCancelledPayment(transactionReference);
            return ResponseEntity.ok(generateCancelResponse());
        } catch (PaymentProcessingException e) {
            log.error("Error processing cancellation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<html><body><h1>Cancellation Failed</h1>" +
                            "<p>There was an error processing your cancellation. Please try again later.</p>" +
                            "<a href=\"/\">Return to Home</a></body></html>");
        }
    }



    @GetMapping("/pay/success")
    public ResponseEntity<?> successPayForPayPal(@RequestParam("paymentId") String paymentId,
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

    private String generateCancelResponse() {
        return "<html><body><h1>Payment Canceled</h1>" +
                "<p>Your payment has been canceled successfully.</p>" +
                "<p>If you wish to retry the payment, click the button below:</p>" +
                "<a href=\"/api/paypal/pay\">Retry Payment</a><br>" +
                "<p>Or return to the home page:</p>" +
                "<a href=\"/\">Return to Home</a>" +
                "</body></html>";
    }

    private String generateSuccessResponse(String paymentId, String payerId) {
        return "<html><body><h1>Payment Successful!</h1>" +
                "<p>Payment ID: " + paymentId + "</p>" +
                "<p>Payer ID: " + payerId + "</p></body></html>";
    }
}