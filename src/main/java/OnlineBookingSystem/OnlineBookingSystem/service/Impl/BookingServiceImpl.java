package OnlineBookingSystem.OnlineBookingSystem.service.Impl;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;

import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final TrainClassService trainClassService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final SeatService seatService;

    private final BookingRepository bookingRepository;



    public BookingResponse createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException {
        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        if (foundUser == null) {
            throw new UserCannotBeFoundException("User with email " + bookTrainDTO.getUserEmail() + " not found");
        }

        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        if (foundTrainClass == null) {
            throw new TrainClassCannotBeFoundException("Train class " + bookTrainDTO.getTrainClassName() + " not found");
        }

        Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSeatNumber());
        Schedule foundSchedule = scheduleService.findByScheduleId(bookTrainDTO.getScheduleId());
        Fare fare = foundTrainClass.getFare();

        if (fare == null) {
            throw new TrainClassCannotBeFoundException("Fare not found for train class: " + foundTrainClass.getClassName());
        }

        Double selectedFareFirst = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare);

        Double totalFare = calculateTotalFare(bookTrainDTO, fare);

        String secondPassengerType = bookTrainDTO.getSecondPassengerType();
        if (bookTrainDTO.getSecondPassengerEmail() != null) {
            if (secondPassengerType == null) {
                throw new InvalidPassengerTypeException("Second passenger type cannot be null");
            }

            if ("minor".equalsIgnoreCase(bookTrainDTO.getPassengerType()) && "minor".equalsIgnoreCase(secondPassengerType)) {
                throw new InvalidPassengerTypeException("Both passengers cannot be minors. At least one must be an adult.");
            }
        } else if ("minor".equalsIgnoreCase(bookTrainDTO.getPassengerType())) {
            throw new InvalidPassengerTypeException("At least one passenger must be an adult.");
        }

        // Create booking for the first passenger
        Booking firstBooking = Booking.builder()
                .fareAmount(selectedFareFirst)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .schedule(foundSchedule)
                .build();
        bookingRepository.save(firstBooking);

        User secondPassenger = null;
        Double selectedFareSecond = null;
        Seat secondBookedSeat = null;

        if (bookTrainDTO.getSecondPassengerEmail() != null) {
            secondPassenger = userService.findUserByEmailOrNull(bookTrainDTO.getSecondPassengerEmail());
            if (secondPassenger == null) {
                secondPassenger = new User();
                secondPassenger.setEmail(bookTrainDTO.getSecondPassengerEmail());
                secondPassenger.setFirstName(bookTrainDTO.getSecondPassengerName());
                secondPassenger.setIdNumber(bookTrainDTO.getSecondPassengerIdNumber());
                secondPassenger.setPhoneNumber(bookTrainDTO.getSecondPassengerPhoneNumber());
                secondPassenger.setIdentificationType(bookTrainDTO.getSecondPassengerIdentificationType());
                secondPassenger = userService.save(secondPassenger);
            }

            selectedFareSecond = getFareForPassengerType(secondPassengerType, fare);


            secondBookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSecondPassengerSeatNumber());

            Booking secondBooking = Booking.builder()
                    .fareAmount(selectedFareSecond)
                    .trainClass(foundTrainClass)
                    .user(secondPassenger)
                    .schedule(foundSchedule)
                    .build();
            bookingRepository.save(secondBooking);
        }

        return getBookingResponse(firstBooking, foundUser, foundTrainClass, bookedSeat, foundSchedule,
                selectedFareFirst, selectedFareSecond, secondPassenger, secondBookedSeat, totalFare);
    }

    private Double getFareForPassengerType(String passengerType, Fare fare) throws InvalidPassengerTypeException {
        if ("adult".equalsIgnoreCase(passengerType)) {
            return fare.getAdultPrices();
        } else if ("minor".equalsIgnoreCase(passengerType)) {
            return fare.getMinorPrices();
        } else {
            throw new InvalidPassengerTypeException("Invalid passenger type: " + passengerType);
        }
    }

    public Double calculateTotalFare(BookTrainDTO bookTrainDTO, Fare fare) throws InvalidPassengerTypeException {
        Double baseFareFirst = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare);
        Double totalFare = baseFareFirst;

        totalFare += 200;
        if (bookTrainDTO.getSecondPassengerEmail() != null) {

            Double baseFareSecond = getFareForPassengerType(bookTrainDTO.getSecondPassengerType(), fare);
            totalFare += baseFareSecond + 200;
        }

        return totalFare;
    }

    private static BookingResponse getBookingResponse(Booking firstBooking, User foundUser, TrainClass foundTrainClass,
                                                      Seat bookedSeat, Schedule foundSchedule,
                                                      Double selectedFareFirst, Double selectedFareSecond,
                                                      User secondPassenger, Seat secondBookedSeat,
                                                      Double totalFare) {
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingId(firstBooking.getBookingId());
        bookingResponse.setMessage("Booking successful");
        bookingResponse.setBookedSeats(bookedSeat.getSeatNumber());
        bookingResponse.setFareAmount(selectedFareFirst);
        bookingResponse.setTotalFareAmount(totalFare);
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


















