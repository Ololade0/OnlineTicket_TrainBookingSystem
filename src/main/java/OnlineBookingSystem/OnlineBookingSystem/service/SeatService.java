package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

import java.util.List;
import java.util.Optional;

public interface SeatService {


List<Seat> generateSeats(int startSeat, int endSeat, TrainClass trainClass);

  Seat bookSeat(int seatNumber);
Seat findSeat(int seatNumber);
Optional<Seat> findSeatById(Long seatId);
void updateSeat(Seat seat);
List<Seat> findAllSeat();

//List<Integer> findSeatNumberByTrainClass(TrainClass trainClass);

}