package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.TrainDto;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Service
//public class TranServiceImpl implements TrainService{
//
//    @Autowired
//    private TrainClassService trainClassService;
//    @Override
//    public Train createNewTrain(TrainDto trainDto) {
//        trainClassService.saveTrainClasses(trainDto.getTrainClassRequests(), trainDto.getStartSeat(), trainDto.getEndSeat());
//
//        Train newTrain = Train.builder()
//                .name(trainDto.getName())
//                .trainClasses(trainDto.getTrainClassRequests())
//
//                .build();
//    }
//}
