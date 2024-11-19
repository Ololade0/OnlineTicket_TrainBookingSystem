package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;

import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateSeatDTO {
    private TrainClass trainClass;
    private int startSeat;
    private int endSeat;
}
