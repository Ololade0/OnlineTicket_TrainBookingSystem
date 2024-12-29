package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatCannotBeFoundException;
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

    @Override
    public Seat findSeat(int seatNumber) {
        return seatRepository.findBySeatNumber(seatNumber).orElseThrow(()->
                new SeatCannotBeFoundException("Seat with number" + seatNumber + "cannot be found"));
    }

    @Override
    public Seat findSeatById(Long seatId) {
        return seatRepository.findById(seatId).orElseThrow(()->
                new SeatCannotBeFoundException("Seat cannot be found"));
    }
    public void updateSeat(Seat seat) {
        seatRepository.save(seat);
    }


}
