package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidSeatNumberException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatAlreadyBookedException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Booking createBooking(BookTrainDTO bookTrainDTO) {

        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getSeatNumber());
        Fare fare = foundTrainClass.getFare();
        Double selectedFare;
        if ("adult".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getMinorPrices();
        } else {
            throw new InvalidPassengerTypeException("Invalid passenger type: " + bookTrainDTO.getPassengerType());
        }
        Booking newBooking = Booking.builder()
                .seats(List.of(bookedSeat))
                .fareAmount(selectedFare)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .build();

        return bookingRepository.save(newBooking);
    }



}



