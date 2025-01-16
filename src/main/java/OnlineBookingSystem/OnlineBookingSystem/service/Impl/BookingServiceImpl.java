package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.PaymentRequest;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
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

    private final PaymentService paymentService;

    @Override
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

        // Payment Processing
        PaymentRequest paymentRequest = paymentProcessing(foundTrainClass, totalFare);
        try {
            Payment payment = paymentService.createPayment(paymentRequest);

            String approvalUrl = payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                    .getHref();

            // Create booking records only after payment is confirmed
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

                selectedFareSecond = getFareForPassengerType(bookTrainDTO.getSecondPassengerType(), fare);
                secondBookedSeat = seatService.bookSeat(bookTrainDTO.getTrainClassName(), bookTrainDTO.getSecondPassengerSeatNumber());

                Booking secondBooking = Booking.builder()
                        .fareAmount(selectedFareSecond)
                        .trainClass(foundTrainClass)
                        .user(secondPassenger)
                        .schedule(foundSchedule)
                        .build();
                bookingRepository.save(secondBooking);
            }

            // Return approval URL for redirection
            return new BookingResponse(firstBooking.getBookingId(),
                    "Redirect to: " + approvalUrl,
                    bookedSeat.getSeatNumber(),
                    selectedFareFirst,
                    totalFare,
                    foundUser);
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Payment could not be processed: " + e.getMessage());
        }
    }

    private static PaymentRequest paymentProcessing(TrainClass foundTrainClass, Double totalFare) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setTotal(totalFare);
        paymentRequest.setCurrency("USD");
        paymentRequest.setPaymentMethod("paypal");
        paymentRequest.setIntent("sale");
        paymentRequest.setDescription("Booking for train class: " + foundTrainClass.getClassName());
        paymentRequest.setCancelUrl("http://localhost:8080/pay/cancel");
        paymentRequest.setSuccessUrl("http://localhost:8080/pay/success");
        return paymentRequest;
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
        Double totalFare = baseFareFirst + 200; // Base fare + additional fees

        if (bookTrainDTO.getSecondPassengerEmail() != null) {
            Double baseFareSecond = getFareForPassengerType(bookTrainDTO.getSecondPassengerType(), fare);
            totalFare += baseFareSecond + 200; // Add second passenger fare + fees
        }

        return totalFare;
    }
}