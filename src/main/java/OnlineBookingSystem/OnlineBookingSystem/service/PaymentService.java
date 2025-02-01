package OnlineBookingSystem.OnlineBookingSystem.service;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.PaymentHistoryResponse;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.List;

public interface PaymentService {
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
    Payment createPayment(PaymentRequest paymentRequest) throws PayPalRESTException;
    List<PaymentHistoryResponse> getPaymentHistory(Long userId);
}
