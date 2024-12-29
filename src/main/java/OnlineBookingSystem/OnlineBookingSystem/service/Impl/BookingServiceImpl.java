package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final TrainClassService trainClassService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final SeatService seatService;
    private final StationService stationService;
    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookTrainDTO bookTrainDTO, FindScheduleDTO findScheduleDTO) {

        scheduleService.findSchedule(findScheduleDTO);
        stationService.findStation(findScheduleDTO.getArrivalStation());
        stationService.findStation(findScheduleDTO.getDepartureStation());

        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        userService.findUserByEmail(bookTrainDTO.getPassengerEmail());

        Seat foundSeat = seatService.findSeatById(bookTrainDTO.getSeatId());
        if (foundSeat.getStatus() == SeatStatus.BOOKED) {
            throw new RuntimeException("Seat is already booked");
        }

        foundSeat.setStatus(SeatStatus.BOOKED);
        seatService.updateSeat(foundSeat);


        Booking newBooking = Booking.builder()
                .passengerEmail(bookTrainDTO.getPassengerEmail())
                .identificationType(bookTrainDTO.getIdentificationType())
                .passengerPhoneNumber(bookTrainDTO.getPassengerPhoneNumber())
                .seats(List.of(foundSeat))
                .trainClass(foundTrainClass)
                .passengerType(bookTrainDTO.getPassengerType())
                .user(userService.findUserByEmail(bookTrainDTO.getPassengerEmail()))
                .build();

        return bookingRepository.save(newBooking);
    }

}
