package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.GenerateSeatDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.TrainClassDto;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.TrainClassRequest;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

import java.util.List;

public interface TrainClassService {
//    TrainClass createTrainClass(List<TrainClass> trainClasses)

    List<TrainClass> saveTrainClasses(List<TrainClassRequest> trainClassRequests, int startSeat, int endSeat);
}
