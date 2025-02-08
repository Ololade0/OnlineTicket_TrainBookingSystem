package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.TrainClassDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.SeatRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainClassRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class trainClassServiceImpl implements TrainClassService {
    @Autowired
    private TrainClassRepository trainClassRepository;
    private final SeatService seatService;

        public TrainClass saveTrainClasses(Train train, TrainClassDTO trainClass, int startSeat, int endSeat) {
            TrainClass newTrainClass = TrainClass.builder()
                    .className(trainClass.getClassName())
                    .fare(trainClass.getFare())
                    .train(train)
                    .build();
            TrainClass savedTrainClass = trainClassRepository.save(newTrainClass);
            List<Seat> generatedSeats = seatService.generateSeats(startSeat, endSeat, savedTrainClass);
            savedTrainClass.setSeats(generatedSeats);
            savedTrainClass.setTotalSeat(generatedSeats.size());

            return trainClassRepository.save(savedTrainClass);
        }

    @Override
    public TrainClass findTrainClassById(Long trainClassId) {
        return trainClassRepository.findById(trainClassId).orElseThrow(()
                -> new TrainCannotBeFoundException("Train Class cannot be found Exception"));

    }

    @Override
    public TrainClass findTrainClassByName(String trainClassName) {
       return trainClassRepository.findByClassName(trainClassName).orElseThrow(()-> new TrainClassCannotBeFoundException("Train Class cannot be found"));
    }

    @Override
    public List<TrainClass> findAll() {
        return trainClassRepository.findAll();
    }


}












