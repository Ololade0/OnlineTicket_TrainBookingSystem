package OnlineBookingSystem.OnlineBookingSystem.dto.request;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookTrainDTO {
    private int seatNumber;
    private String userEmail;
    private String passengerPhoneNumber;
    private String trainClassName;
    private String passengerType;
    private Long scheduleId;


}
