package OnlineBookingSystem.OnlineBookingSystem.exceptions;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message) {
        super(message);
    }
}
