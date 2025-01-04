package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;

import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final TrainClassService trainClassService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final SeatService seatService;
    private final StationService stationService;
    private final BookingRepository bookingRepository;



    @Override
    public BookingResponse createBooking(BookTrainDTO bookTrainDTO) {
        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSeatNumber());
        Schedule foundSchedule = scheduleService.findByScheduleId(bookTrainDTO.getScheduleId());
        Fare fare = foundTrainClass.getFare();
        Double selectedFare;
        if ("adult".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            selectedFare = fare.getMinorPrices();
        }
        else {
            throw new InvalidPassengerTypeException("Invalid passenger type: " + bookTrainDTO.getPassengerType());
        }
        Booking newBooking = Booking.builder()
                .fareAmount(selectedFare)
                .trainClass(foundTrainClass)
                .user(foundUser)
               .schedule(foundSchedule)
                .build();
          bookingRepository.save(newBooking);
      return getBookingResponse(newBooking,foundUser, foundTrainClass, bookedSeat, foundSchedule, selectedFare);

    }


    private static BookingResponse getBookingResponse(Booking booking, User foundUser, TrainClass foundTrainClass, Seat bookedSeat, Schedule foundSchedule, Double selectedFare) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(booking.getBookingId());
        bookingResponse.setMessage("Booking successful");
        bookingResponse.setBookedSeat(bookedSeat.getSeatNumber());
        bookingResponse.setFareAmount(selectedFare);
        bookingResponse.setUser(foundUser);
                return bookingResponse;
    }



}



