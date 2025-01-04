package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AddTrainClassToTrainDTO {

    private List<TrainClassDTO> trainClass;
    private String trainName;
    private String trainCode;
    private int startSeat;
    private int endSeat;
}
