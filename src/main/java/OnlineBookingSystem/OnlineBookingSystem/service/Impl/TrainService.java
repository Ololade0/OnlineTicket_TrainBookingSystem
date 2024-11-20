package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.TrainDto;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;

public interface TrainService {
    Train createNewTrain(TrainDto train);
}
