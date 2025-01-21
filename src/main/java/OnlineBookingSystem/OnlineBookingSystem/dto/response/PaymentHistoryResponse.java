package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryResponse {
    private String paymentId;
    private Double amount;
    private String currency;
    private String status;
    private String date;

    public PaymentHistoryResponse(Long id, Double totalPrice, String currency, PaymentStatus paymentStatus, String date) {
    }
}
