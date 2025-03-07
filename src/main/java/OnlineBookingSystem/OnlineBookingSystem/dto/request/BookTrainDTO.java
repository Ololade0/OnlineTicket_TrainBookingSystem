package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class BookTrainDTO {
    private String trainClassName;
    private Long scheduleId;
    private String userEmail;
    private String passengerType;
    private int seatNumber;
   private List<OtherPassenger> additionalPassengers = new ArrayList<>();
   private PaymentMethod paymentMethod;
   private int count;

}