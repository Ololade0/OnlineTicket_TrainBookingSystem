package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PassengerDTO {
    private String email;
    private String firstName;
    private String idNumber;
    private String phoneNumber;
    private IdentificationType identificationType;
    private String passengerType; // "adult" or "minor"
    private int seatNumber;
}
