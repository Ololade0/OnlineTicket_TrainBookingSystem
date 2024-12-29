package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;

import java.util.List;

public interface SeatService {

List<Seat> generateSeats(int startSeat, int endSeat, TrainClass trainClass);
Seat findSeat(int seatNumber);
Seat findSeatById(Long seatId);
    void updateSeat(Seat seat);



}
