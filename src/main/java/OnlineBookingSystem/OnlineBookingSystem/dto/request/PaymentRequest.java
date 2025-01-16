package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Double total;
    private String currency;
    private String paymentMethod;
    private String intent;
    private String description;
    private String cancelUrl;
    private String successUrl;
}
