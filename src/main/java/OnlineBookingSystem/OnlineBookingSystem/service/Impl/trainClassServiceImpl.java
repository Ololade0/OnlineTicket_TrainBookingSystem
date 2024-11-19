package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.TrainClassRequest;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainClassRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class trainClassServiceImpl implements TrainClassService {
    @Autowired
    private TrainClassRepository trainClassRepository;
    private final SeatService seatService;






@Transactional
    public List<TrainClass> saveTrainClasses(List<TrainClassRequest> trainClassRequests, int startSeat, int endSeat) {
        List<Seat> generatedSeats =   seatService.generateSeats(startSeat, endSeat);
        List<TrainClass> trainClasses = new ArrayList<>();
        for (TrainClassRequest request : trainClassRequests) {
            TrainClass trainClasse = TrainClass.builder()
                    .className(request.getClassName())
                    .fare(request.getFare())
                    .seats(generatedSeats)
                    .build();
            trainClasses.add(trainClasse);
        }
        return trainClassRepository.saveAll(trainClasses);
    }



}
