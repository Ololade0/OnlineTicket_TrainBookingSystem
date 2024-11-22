package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddTrainClassToTrainDTO {
//    public Long trainId;
    private TrainClass trainClass;
//    private Train train;
    private String className;
    private int startSeat;
    private int endSeat;
//    private
}
