package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BookTrainDTO {
    private String passengerType;
    private String passengerName;
    private IdentificationType identificationType;
    private String trainClassName;
    private String passengerEmail;
    private String passengerPhoneNumber;
    private List<Seat> seat;
    private User user;
    private Fare fare;
    private long seatId;
    private long stationId;
    private TrainClass trainClass;


}
