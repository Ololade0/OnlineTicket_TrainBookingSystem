package OnlineBookingSystem.OnlineBookingSystem.dto.request;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookTrainDTO {

    private String userEmail;
    private String trainClassName;
    private int seatNumber;
    private Long scheduleId;
    private String passengerType;
    private String secondPassengerType;
    private String secondPassengerEmail;
    private String secondPassengerName;
    private String secondPassengerIdNumber;
    private String secondPassengerPhoneNumber;
    private int secondPassengerSeatNumber;
    private IdentificationType secondPassengerIdentificationType;
    private GenderType genderType;

}
