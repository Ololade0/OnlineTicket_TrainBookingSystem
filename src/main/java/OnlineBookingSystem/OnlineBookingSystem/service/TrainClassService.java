package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.TrainClassDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

import java.util.List;

public interface TrainClassService {

    TrainClass saveTrainClasses(Train train, TrainClassDTO trainClass, int startSeat, int endSeat);
   TrainClass findTrainClassById(Long trainClassId);
   TrainClass findTrainClassByName(String trainClassName);

    List<TrainClass> findAll();
}
