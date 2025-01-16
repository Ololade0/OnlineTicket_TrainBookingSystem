package OnlineBookingSystem.OnlineBookingSystem.service;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
    Payment createPayment(PaymentRequest paymentRequest) throws PayPalRESTException;
}
