package OnlineBookingSystem.OnlineBookingSystem.service;


import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.AddTrainClassToTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;

public interface TrainService {
//    Train createNewTrain(Train train);
////    Train addTrainClassToTrain(AddTrainClassToTrainDTO AddTrainClassToTrainD)
    Train createNewTrains(AddTrainClassToTrainDTO addTrainClassToTrainDTO);
    Train findTrainById(Long trainId);

//    Train addTrainClassToTrain(AddTrainClassToTrainDTO AddTrainClassToTrainD);
}
