package OnlineBookingSystem.OnlineBookingSystem.controller.PaymentControllers;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.BookingPayment;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
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

    public String createPaymentIntentForStripe(@RequestParam Double totalFare, User user, Booking booking, Model model) {
        String clientSecret = stripeService.processStripePayment(totalFare,user,booking);
        model.addAttribute("clientSecret", clientSecret);
        return "checkout";
    }


//
@PostMapping("stripe-webhook")
public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                            @RequestHeader("Stripe-Signature") String sigHeader) {
    try {
        // Verify the webhook signature
        Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

        // Log the event type
        System.out.println("Webhook verified: " + event.getType());

        // Process different Stripe event types
        switch (event.getType()) {
            case "checkout.session.completed":
                System.out.println("✅ Payment completed!");
                break;
            case "payment_intent.succeeded":
                System.out.println("✅ Payment intent succeeded!");
                break;
            case "customer.created":
                System.out.println("✅ New customer created!");
                break;
            default:
                System.out.println("⚠️ Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Webhook received successfully");

    } catch (SignatureVerificationException e) {
        System.out.println("❌ Invalid signature!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
    }
}

}






