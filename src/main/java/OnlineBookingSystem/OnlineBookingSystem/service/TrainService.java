package OnlineBookingSystem.OnlineBookingSystem.service;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.AddTrainClassToTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;

public interface TrainService {
    Train createNewTrains(AddTrainClassToTrainDTO addTrainClassToTrainDTO);
    Train findTrainById(Long trainId);


}
