package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;



    // STRIPE PAYMENT INTEGRATION
    public String processStripePayment(Double totalFare) {
        try {
            Stripe.apiKey = stripeApiKey;
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
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
