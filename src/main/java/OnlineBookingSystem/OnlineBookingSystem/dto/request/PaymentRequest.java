package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.Currency;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String email;
    private Double total;
    private PaymentMethod paymentMethod;
    private String intent;
    private String description;
    private String cancelUrl;
    private String successUrl;
    private Currency currency;
    private Booking booking;
}
