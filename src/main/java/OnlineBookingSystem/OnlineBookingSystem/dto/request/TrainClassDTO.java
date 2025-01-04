package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainClassDTO {
    private String className;
    private int totalSeats;
    private Fare fare;


}