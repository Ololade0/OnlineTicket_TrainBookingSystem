package OnlineBookingSystem.OnlineBookingSystem.dto.request;//package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class BookTrainDTO {
    private int seatNumber;
//    private Long userId;
    private String userEmail;
    private String passengerPhoneNumber;
    private String trainClassName;
    private String passengerType;


}
