package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final TrainClassService trainClassService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final SeatService seatService;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final OtherPassengerRepository otherPassengerRepository;

    public BookingResponse createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException, IOException, InterruptedException {
        log.debug("Starting createBooking for user: {}", bookTrainDTO.getUserEmail());

        // Validate user
        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        if (foundUser == null) {
            throw new UserCannotBeFoundException("User with email " + bookTrainDTO.getUserEmail() + " not found");
        }

        // Validate train class
        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());
        if (foundTrainClass == null) {
            throw new TrainClassCannotBeFoundException("Train class " + bookTrainDTO.getTrainClassName() + " not found");
        }

        Schedule foundSchedule = scheduleService.findByScheduleId(bookTrainDTO.getScheduleId());
        Fare fare = foundTrainClass.getFare();
        if (fare == null) {
            throw new TrainClassCannotBeFoundException("Fare not found for train class: " + foundTrainClass.getClassName());
        }

        // Calculate total fare
        Double totalFare = calculateTotalFare(bookTrainDTO, fare);

        // Create primary booking (without booking a seat yet)
        Booking primaryBooking = Booking.builder()
                .bookingDate(LocalDateTime.now())
                .fareAmount(totalFare)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .schedule(foundSchedule)
                .build();
        primaryBooking = bookingRepository.save(primaryBooking);

        // Process payment
        String approvalUrl = paymentService.paymentProcessings(totalFare, foundUser, primaryBooking, foundUser.getEmail(), bookTrainDTO.getPaymentMethod());
        if (approvalUrl != null) {
            Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSeatNumber());
            primaryBooking.setFareAmount(getFareForPassengerType(bookTrainDTO.getPassengerType(), fare));
            bookingRepository.save(primaryBooking);

            List<OtherPassenger> savedAdditionalPassengers = bookTrainForOtherPassengers(bookTrainDTO, foundUser, fare, primaryBooking);
            return getBookingResponse(foundUser, bookedSeat, primaryBooking.getFareAmount(), totalFare, primaryBooking, savedAdditionalPassengers, primaryBooking.getBookingDate(), approvalUrl);
        } else {
            throw new RuntimeException("Payment was not successful, seat will not be booked.");
        }
    }

    private static BookingResponse getBookingResponse(
            User foundUser,
            Seat bookedSeat,
            Double primaryPassengerFare,
            Double totalFare,
            Booking primaryBooking,
            List<OtherPassenger> savedAdditionalPassengers,
            LocalDateTime bookingDate, String approvalUrl) {

        return new BookingResponse(
                primaryBooking.getBookingId(),
                "Booking successful",
                bookedSeat.getSeatNumber(),
                primaryPassengerFare,
                totalFare,
                User.builder()
                        .id(foundUser.getId())
                        .firstName(foundUser.getFirstName())
                        .lastName(foundUser.getLastName())
                        .email(foundUser.getEmail())
                        .phoneNumber(foundUser.getPhoneNumber())
                        .idNumber(foundUser.getIdNumber())
                        .identificationType(foundUser.getIdentificationType())
                        .build(),
                bookingDate,
                savedAdditionalPassengers,
                approvalUrl
        );
    }


    private List<OtherPassenger> bookTrainForOtherPassengers(BookTrainDTO bookTrainDTO, User foundUser, Fare fare, Booking primaryBooking) throws InvalidPassengerTypeException {
        List<OtherPassenger> savedAdditionalPassengers = new ArrayList<>();
        if (bookTrainDTO.getAdditionalPassengers() != null) {
            for (OtherPassenger additionalPassenger : bookTrainDTO.getAdditionalPassengers()) {
                Seat additionalSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), additionalPassenger.getSeatNumber());
                Double additionalPassengerFare = getFareForPassengerType(additionalPassenger.getPassengerType(), fare);

                // Save additional passenger
                OtherPassenger savedPassenger = OtherPassenger.builder()
                        .name(additionalPassenger.getName())
                        .email(additionalPassenger.getEmail())
                        .gender(additionalPassenger.getGender())
                        .phoneNumber(additionalPassenger.getPhoneNumber())
                        .idNumber(additionalPassenger.getIdNumber())
                        .identificationType(additionalPassenger.getIdentificationType())
                        .passengerType(additionalPassenger.getPassengerType())
                        .seatNumber(additionalSeat.getSeatNumber())
                        .booking(primaryBooking)
                        .user(foundUser)
                        .build();
                otherPassengerRepository.save(savedPassenger);
                savedAdditionalPassengers.add(savedPassenger);
            }
        }

        return savedAdditionalPassengers;
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


    private Double calculateTotalFare(BookTrainDTO bookTrainDTO, Fare fare) throws InvalidPassengerTypeException {
        Double totalFare = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare) + 200;

        if (bookTrainDTO.getAdditionalPassengers() != null) {
            for (OtherPassenger passenger : bookTrainDTO.getAdditionalPassengers()) {
                totalFare += getFareForPassengerType(passenger.getPassengerType(), fare) + 200;
            }
        }
        return totalFare;

    }

}


