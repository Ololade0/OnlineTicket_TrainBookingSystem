package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainClassRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class trainClassServiceImpl implements TrainClassService {
    @Autowired
    private TrainClassRepository trainClassRepository;
    private final SeatService seatService;


//
//@Transactional
//    public List<TrainClass> saveTrainClasses(List<TrainClass> trainClassRequests, int startSeat, int endSeat) {
//        List<Seat> generatedSeats =   seatService.generateSeats(startSeat, endSeat);
//        List<TrainClass> trainClasses = new ArrayList<>();
//        for (TrainClass request : trainClassRequests) {
//            TrainClass trainClasse = TrainClass.builder()
//                    .className(request.getClassName())
//                    .fare(request.getFare())
//                    .seats(generatedSeats)
//                    .build();
//            trainClasses.add(trainClasse);
//        }
//        return trainClassRepository.saveAll(trainClasses);
//    }

    //
//@Transactional
//    public TrainClass saveTrainClasses(TrainClass trainClass, int startSeat, int endSeat) {
//        List<Seat> generatedSeats =   seatService.generateSeats(startSeat, endSeat, trainClass);
//            TrainClass newTrainClass = TrainClass.builder()
//                    .className(trainClass.getClassName())
//                    .fare(trainClass.getFare())
//                    .seats(generatedSeats)
//                    .totalSeat(trainClass.getTotalSeat())
//                    .build();
//// rtrainClassRepository.save(newTrainClass);
////        return savedTrain;
////    List<Seat> generatedSeats =   seatService.generateSeats(startSeat, endSeat, trainClass);
////    newTrainClass.setSeats(generatedSeats);
////    newTrainClass.setTotalSeat(generatedSeats.size());
//    return trainClassRepository.save(newTrainClass);

    @Transactional
    public TrainClass saveTrainClasses(TrainClass trainClass, int startSeat, int endSeat) {
        TrainClass newTrainClass = TrainClass.builder()
                .className(trainClass.getClassName())
                .fare(trainClass.getFare())
                .build();
        // Save the train class first to make it a managed entity
        TrainClass savedTrainClass = trainClassRepository.save(newTrainClass);
        // Generate seats and associate them with the saved train class
        List<Seat> generatedSeats = seatService.generateSeats(startSeat, endSeat, savedTrainClass);

        // Update the saved train class with the generated seats
        savedTrainClass.setSeats(generatedSeats);
        savedTrainClass.setTotalSeat(generatedSeats.size()); // Update total seats if needed

        // Save the train class again with updated seats
      return trainClassRepository.save(savedTrainClass);

    }





}

