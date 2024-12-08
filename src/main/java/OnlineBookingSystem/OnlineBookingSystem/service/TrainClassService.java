package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

import java.util.List;

public interface TrainClassService {

    public TrainClass saveTrainClasses(Train train, TrainClass trainClass, int startSeat, int endSeat);
   TrainClass findTrainClassById(Long trainClassId);

    List<TrainClass> findAll();
}
