package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.SeatRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;




    public List<Seat> generateSeats(int startSeat, int endSeat, TrainClass trainClass) {
        List<Seat> seats = new ArrayList<>();
        for (int i = startSeat; i <= endSeat; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i);
            seat.setStatus(SeatStatus.AVAILBALE);
            seat.setTrainClass(trainClass);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
        return seats;
    }
}
