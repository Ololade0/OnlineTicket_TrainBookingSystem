package OnlineBookingSystem.OnlineBookingSystem.service.Impl.PaymentServiceImpl;

public interface StripeService {
    String processStripePayment(Double totalFare);
}
