package OnlineBookingSystem.OnlineBookingSystem.service.Impl;//package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidSeatNumberException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatAlreadyBookedException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.SeatRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {




private final SeatRepository seatRepository;

public List<Seat> generateSeats(int startSeat, int endSeat, TrainClass trainClass){
        List<Seat> seats=new ArrayList<>();
        for(int i=startSeat;i<=endSeat;i++){
        Seat seat=new Seat();
        seat.setSeatNumber(i);
        seat.setStatus(SeatStatus.AVAILABLE);
        seat.setTrainClass(trainClass);
        seats.add(seat);
        }
        seatRepository.saveAll(seats);
        return seats;

        }


    @Override
    public Seat bookSeat(String trainClassName, int seatNumber) {
        Optional<Seat> foundSeat = seatRepository.findBySeatNumberAndTrainClass_ClassName(trainClassName, seatNumber);

        if (foundSeat.isEmpty()){
            throw new InvalidSeatNumberException("Seat number " + seatNumber + " does not exist.");

        }
        if (foundSeat.get().getStatus() == SeatStatus.BOOKED) {
            throw new SeatAlreadyBookedException("Seat number " + seatNumber + " has already been booked");
        }


        Seat seatToBook = foundSeat.get();
        seatToBook.setStatus(SeatStatus.BOOKED);
        return seatRepository.save(seatToBook);
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



}
