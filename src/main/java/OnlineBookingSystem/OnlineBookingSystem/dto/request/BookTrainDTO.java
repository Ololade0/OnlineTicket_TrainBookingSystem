package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
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
//    private List<PassengerDTO> additionalPassengers = new ArrayList<>();
    private String secondPassengerEmail;
    private String secondPassengerName;
    private String secondPassengerIdNumber;
    private String secondPassengerPhoneNumber;
    private IdentificationType secondPassengerIdentificationType;
    private String secondPassengerType;
    private int secondPassengerSeatNumber;

}