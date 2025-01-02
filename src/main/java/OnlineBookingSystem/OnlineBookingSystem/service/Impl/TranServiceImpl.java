package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.AddTrainClassToTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@Transactional
public class TranServiceImpl implements TrainService {

    @Autowired
    private TrainClassService trainClassService;

    @Autowired
    private TrainRepository trainRepository;




    public Train createNewTrains(AddTrainClassToTrainDTO addTrainClassToTrainDTO) {
        Train newTrain = Train.builder()
                .trainName(addTrainClassToTrainDTO.getTrainName())
                .trainCode(addTrainClassToTrainDTO.getTrainCode())
                .build();
        Train savedTrain = trainRepository.save(newTrain);

        if (addTrainClassToTrainDTO.getTrainClass() != null) {
            for (TrainClass trainClass : addTrainClassToTrainDTO.getTrainClass()) {
                TrainClass savedTrainClass = trainClassService.saveTrainClasses(
                        savedTrain,
                        trainClass,
                        addTrainClassToTrainDTO.getStartSeat(),
                        addTrainClassToTrainDTO.getEndSeat()
                );

                if (savedTrain.getTrainClasses() == null) {
                    savedTrain.setTrainClasses(new ArrayList<>());
                }
                savedTrain.getTrainClasses().add(savedTrainClass);
            }
        }

        return trainRepository.save(savedTrain);
    }




    @Override
        public Train findTrainById(Long trainId) {
            return trainRepository.findById(trainId)
                    .orElseThrow(() -> new TrainCannotBeFoundException("Train with ID " + trainId + " cannot be found"));
        }
    }

