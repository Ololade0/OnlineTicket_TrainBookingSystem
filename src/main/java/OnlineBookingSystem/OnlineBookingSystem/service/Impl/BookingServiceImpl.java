package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.PaymentRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
//    private final OtherPassengerService otherPassengerService;


    private final OtherPassengerRepository otherPassengerRepository;

public BookingResponse createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException {
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

    // Book seat for primary user
    Seat bookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSeatNumber());
    Double primaryPassengerFare = getFareForPassengerType(bookTrainDTO.getPassengerType(), fare);
    Double totalFare = calculateTotalFare(bookTrainDTO, fare);

    String approvalUrl = paymentProcessing(totalFare);

    // Create primary booking
    Booking primaryBooking = Booking.builder()
            .bookingDate(LocalDateTime.now())
            .fareAmount(primaryPassengerFare)
            .trainClass(foundTrainClass)
            .user(foundUser)
            .schedule(foundSchedule)
            .build();
    primaryBooking = bookingRepository.save(primaryBooking);

    if (primaryBooking.getBookingId() == null) {
        throw new RuntimeException("Failed to save primary booking.");
    }
    List<OtherPassenger> savedAdditionalPassengers = bookTrainForOtherPassengers(bookTrainDTO, foundUser, fare, primaryBooking);
    return getBookingResponse(foundUser, bookedSeat, primaryPassengerFare, totalFare, primaryBooking, savedAdditionalPassengers, primaryBooking.getBookingDate(), approvalUrl);
}

    private String paymentProcessing(Double totalFare) {
        String approvalUrl;
        try {
            // Prepare the payment request
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setCurrency("USD");
            paymentRequest.setTotal(totalFare);
            paymentRequest.setPaymentMethod(PaymentMethod.PAYPAL);
            paymentRequest.setDescription("Train ticket booking");
            paymentRequest.setIntent("sale");
            paymentRequest.setCancelUrl("http://localhost:8080/api/payments/pay/cancel");
            paymentRequest.setSuccessUrl("http://localhost:8080/api/payments/pay/success");

            // Create payment and get the response
            Payment payment = paymentService.createPayment(paymentRequest);
            log.debug("Payment object: {}", payment);

            // Extract the approval URL from the response
            approvalUrl = payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(com.paypal.api.payments.Links::getHref)
                    .orElseThrow(() -> new RuntimeException("Approval URL not found in the payment response."));
        } catch (PayPalRESTException e) {
            log.error("Payment failed: {}", e.getMessage());
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }

        if (approvalUrl == null) {
            throw new RuntimeException("Approval URL not found. Payment creation might have failed.");
        }
        return approvalUrl;
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
                "Approval url" + approvalUrl
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
                        .booking(primaryBooking) // Ensure this is set
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

