package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.SeatAlreadyBookedException;
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
        // Validate user
        User foundUser = userService.findUserById(bookTrainDTO.getUserId());
        if (foundUser == null) {
            throw new IllegalArgumentException("User not found for ID: " + bookTrainDTO.getUserId());
        }

        Optional<Seat> foundSeat = seatService.findSeatByNumber(bookTrainDTO.getSeatNumber());
        if(foundSeat.isEmpty() || foundSeat.get().getStatus() == SeatStatus.BOOKED ){
            throw new SeatAlreadyBookedException("Seat number " + foundSeat + "has already been booked");
        }
        foundSeat.get().setStatus(SeatStatus.BOOKED);
        seatService.updateSeat(foundSeat.get());

        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        if (foundTrainClass == null) {
            throw new IllegalArgumentException("Train class not found: " + bookTrainDTO.getTrainClassName());
        }
        Fare fare = foundTrainClass.getFare();
        Double selectedFare;
        if ("adult".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getMinorPrices();
        } else {
            throw new IllegalArgumentException("Invalid passenger type: " + bookTrainDTO.getPassengerType());
        }

        // Create and save the booking
        Booking newBooking = Booking.builder()
                .seats(List.of(foundSeat.get()))
                .fareAmount(selectedFare)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .build();

        return bookingRepository.save(newBooking);
    }



}



