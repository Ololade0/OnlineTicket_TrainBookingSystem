package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

public interface TrainClassService {

    public TrainClass saveTrainClasses(Train train, TrainClass trainClass, int startSeat, int endSeat);
}
