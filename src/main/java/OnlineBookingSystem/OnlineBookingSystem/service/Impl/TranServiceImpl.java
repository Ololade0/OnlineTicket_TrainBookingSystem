package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.AddTrainClassToTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TranServiceImpl implements TrainService {

    @Autowired
    private TrainClassService trainClassService;

    @Autowired
    private TrainRepository trainRepository;
//    @Override
//    public Train createNewTrain(Train train) {
//        Train newTrain = Train.builder()
//                .name(train.getName())
//                .build();
//        Train savedTrain = trainRepository.save(newTrain);
//
//
//
//    return savedTrain;
//
//    }


//    @Override
//
//
//    public Train createNewTrains(Train train, AddTrainClassToTrainDTO addTrainClassToTrainDTO) {
//        Train newTrain = Train.builder()
//                .name(train.getName())
//                .build();
//        Train savedTrain = trainRepository.save(newTrain);
//        TrainClass savedTrainClass = trainClassService.saveTrainClasses(addTrainClassToTrainDTO.getTrainClass(), addTrainClassToTrainDTO.getStartSeat(), addTrainClassToTrainDTO.getEndSeat());
//        Optional<Train> foundTrain = trainRepository.findById(savedTrain.getId());
//
//        foundTrain.get().getTrainClasses().add(savedTrainClass);
//        return trainRepository.save(foundTrain.get());
//
//    }

    public Train createNewTrains(AddTrainClassToTrainDTO addTrainClassToTrainDTO) {
        Train newTrain = Train.builder()
                .name(addTrainClassToTrainDTO.getClassName())
                .build();
        Train savedTrain = trainRepository.save(newTrain);

        // Save TrainClass and associate with the Train
        TrainClass savedTrainClass = trainClassService.saveTrainClasses(
                addTrainClassToTrainDTO.getTrainClass(),
                addTrainClassToTrainDTO.getStartSeat(),
                addTrainClassToTrainDTO.getEndSeat()
        );

        // Ensure the trainClasses list is initialized and add the new TrainClass
        if (savedTrain.getTrainClasses() == null) {
            savedTrain.setTrainClasses(new ArrayList<>());  // Initialize if null
        }
        savedTrain.getTrainClasses().add(savedTrainClass);

        return trainRepository.save(savedTrain);
    }




    @Override
    public Train findTrainById(Long trainId) {
       return trainRepository.findById(trainId).orElseThrow(()-> new TrainCannotBeFoundException("Train with ID " + trainId + "cannot be found"));

    }

//    @Override
//    public Train addTrainClassToTrain(AddTrainClassToTrainDTO AddTrainClassToTrainD) {
//       TrainClass savedTrainClass = trainClassService.saveTrainClasses(AddTrainClassToTrainD.getTrainClass(), AddTrainClassToTrainD.getStartSeat(), AddTrainClassToTrainD.getEndSeat());
//     Optional<Train> foundTrain =   trainRepository.findById(AddTrainClassToTrainD.getTrainId());
//     if(foundTrain.isPresent()){
//         foundTrain.get().getTrainClasses().add(savedTrainClass);
//       return trainRepository.save(foundTrain.get());
//     }
//     throw new TrainCannotBeFoundException("Train with ID " + foundTrain + "cannot be found");
//    }
}
