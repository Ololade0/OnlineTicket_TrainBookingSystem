package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.SignUpUserResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
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

    private final BookingRepository bookingRepository;





    public BookingResponse createBooking(BookTrainDTO bookTrainDTO) {
        // Find the user for the first passenger
        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        if (foundUser == null) {
            throw new UserCannotBeFoundException("User with email " + bookTrainDTO.getUserEmail() + " not found");
        }

        // Find train class
        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        if (foundTrainClass == null) {
            throw new TrainClassCannotBeFoundException("Train class " + bookTrainDTO.getTrainClassName() + " not found");
        }

        // Book seat for the first passenger
        Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSeatNumber());
        Schedule foundSchedule = scheduleService.findByScheduleId(bookTrainDTO.getScheduleId());
        Fare fare = foundTrainClass.getFare();

        if (fare == null) {
            throw new TrainClassCannotBeFoundException("Fare not found for train class: " + foundTrainClass.getClassName());
        }

        // Calculate fare for the first passenger
        Double selectedFareFirst = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare);

        // Create booking for the first passenger
        Booking firstBooking = Booking.builder()
                .fareAmount(selectedFareFirst)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .schedule(foundSchedule)
                .build();
        bookingRepository.save(firstBooking);

        User secondPassenger = null;
        Double selectedFareSecond = null; // Initialize to null for optional second passenger
        Seat secondBookedSeat = null;

        // Check if second passenger details are provided
        if (bookTrainDTO.getSecondPassengerEmail() != null) {
            secondPassenger = userService.findUserByEmailOrNull(bookTrainDTO.getSecondPassengerEmail());
            if (secondPassenger == null) {
                secondPassenger = new User();
                secondPassenger.setEmail(bookTrainDTO.getSecondPassengerEmail());
                secondPassenger.setFirstName(bookTrainDTO.getSecondPassengerName());
                secondPassenger.setIdNumber(bookTrainDTO.getSecondPassengerIdNumber());
                secondPassenger.setPhoneNumber(bookTrainDTO.getSecondPassengerPhoneNumber());
                secondPassenger.setIdentificationType(bookTrainDTO.getSecondPassengerIdentificationType());
            }

            // Ensure secondPassengerType is not null
            String secondPassengerType = bookTrainDTO.getSecondPassengerType();
            if (secondPassengerType == null) {
                throw new InvalidPassengerTypeException("Second passenger type cannot be null");
            }

            selectedFareSecond = getFareForPassengerType(secondPassengerType, fare);

            // Book seat for the second passenger
            secondBookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSecondPassengerSeatNumber());

            // Create booking for the second passenger
            Booking secondBooking = Booking.builder()
                    .fareAmount(selectedFareSecond)
                    .trainClass(foundTrainClass)
                    .user(secondPassenger)
                    .schedule(foundSchedule)
                    .build();
            bookingRepository.save(secondBooking);
        }

        // Return response with both bookings
        return getBookingResponse(firstBooking, foundUser, foundTrainClass, bookedSeat, foundSchedule,
                selectedFareFirst, selectedFareSecond, secondPassenger, secondBookedSeat);
    }

    private Double getFareForPassengerType(String passengerType, Fare fare) {
        if ("adult".equalsIgnoreCase(passengerType)) {
            return fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(passengerType)) {
            return fare.getMinorPrices();
        } else {
            throw new InvalidPassengerTypeException("Invalid passenger type: " + passengerType);
        }
    }

    private static BookingResponse getBookingResponse(Booking firstBooking, User foundUser, TrainClass foundTrainClass,
                                                      Seat bookedSeat, Schedule foundSchedule,
                                                      Double selectedFareFirst, Double selectedFareSecond,
                                                      User secondPassenger, Seat secondBookedSeat) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(firstBooking.getBookingId());
        bookingResponse.setMessage("Booking successful for " + (secondPassenger != null ? "both passengers" : "the first passenger"));

        // Handle booked seats as concatenated strings
        String bookedSeats;
        if (secondBookedSeat != null) {
            bookedSeats = (bookedSeat.getSeatNumber()) + ", " +(secondBookedSeat.getSeatNumber());
        } else {
            bookedSeats = String.valueOf(bookedSeat.getSeatNumber());
        }
        bookingResponse.setBookedSeats((bookedSeats));

        bookingResponse.setFareAmount(selectedFareFirst);
        bookingResponse.setUser(foundUser);

        if (secondPassenger != null) {
            bookingResponse.setSecondPassengerEmail(secondPassenger.getEmail());
            bookingResponse.setSecondPassengerName(secondPassenger.getFirstName());
            bookingResponse.setSecondPassengerIdNumber(secondPassenger.getIdNumber());
            bookingResponse.setSecondPassengerPhoneNumber(secondPassenger.getPhoneNumber());
            bookingResponse.setSecondPassengerSeatNumber(secondBookedSeat.getSeatNumber());
            bookingResponse.setSecondPassengerFareAmount(selectedFareSecond);
        }

        return bookingResponse;
    }



}