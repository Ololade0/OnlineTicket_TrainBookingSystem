package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

import java.io.IOException;

public interface PayStackService {
    String processPaystackPayment(String email, Double amount) throws IOException, InterruptedException;
    String verifyPaystackPaymentTransaction(String reference) throws IOException;
}
