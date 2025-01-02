package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.SeatRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    public List<Seat> generateSeats(int startSeat, int endSeat, TrainClass trainClass) {
//        TrainClass foundTrainClass = trainClassService.findTrainClassById(trainClass.getTrainClassId());
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
    public Optional<Seat> findSeatById(Long seatId) {
        return Optional.ofNullable(seatRepository.findById(seatId).orElseThrow(() ->
                new SeatCannotBeFoundException("Seat cannot be found")));
    }
    public void updateSeat(Seat seat) {
        seatRepository.save(seat);
    }

    @Override
    public List<Seat> findAllSeat() {
        return seatRepository.findAll();
    }

    @Override
    public Optional<Seat> findSeatByNumber(int seatNumber) {
        return seatRepository.findBySeatNumber(seatNumber);
    }


}
