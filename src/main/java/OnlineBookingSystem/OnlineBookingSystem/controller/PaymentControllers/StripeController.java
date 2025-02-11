package OnlineBookingSystem.OnlineBookingSystem.controller.PaymentControllers;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;
    private final StripeService stripeService;



    @GetMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntentForStripe(@RequestParam Double totalFare,
                                                               @RequestParam Long userId,
                                                               @RequestParam Long bookingId) {

        String clientSecret = stripeService.processStripePayment(totalFare, userId, bookingId);
        return ResponseEntity.ok(clientSecret);
    }
      @PostMapping("stripe-webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            if ("payment_intent.succeeded".equals(event.getType())) {
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (paymentIntent != null) {
                    String paymentIntentId = paymentIntent.getId();
                    stripeService.confirmPayment(paymentIntentId);
                }
            }
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }


}






