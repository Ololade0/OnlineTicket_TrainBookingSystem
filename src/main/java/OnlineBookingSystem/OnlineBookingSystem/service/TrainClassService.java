package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

public interface TrainClassService {
//    TrainClass createTrainClass(List<TrainClass> trainClasses)

    TrainClass saveTrainClasses(TrainClass trainClass, int startSeat, int endSeat);
}
