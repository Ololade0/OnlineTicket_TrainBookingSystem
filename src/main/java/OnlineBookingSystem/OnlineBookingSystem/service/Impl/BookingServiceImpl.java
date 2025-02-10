package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.TrainClassCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.UserCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.OtherPassengerRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import OnlineBookingSystem.OnlineBookingSystem.utils.PnrCodeGenerator;
import ch.qos.logback.core.testUtil.RandomUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private final OtherPassengerService otherPassengerService;

    private final PnrCodeGenerator pnrCodeGenerator;
@Transactional
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

        Double totalFare = otherPassengerService.calculateTotalFare(bookTrainDTO, fare);

        // Create primary booking (without booking a seat yet)
        Booking primaryBooking = Booking.builder()
                .bookingDate(LocalDateTime.now())
               .totalFareAmount(totalFare)
                .trainClass(foundTrainClass)
                .user(foundUser)
                .schedule(foundSchedule)
                .bookingDate(LocalDateTime.now())
                .seatNumber(bookTrainDTO.getSeatNumber())
                .passengerType(bookTrainDTO.getPassengerType())
                .travelDate(foundSchedule.getDepartureDate().atStartOfDay())
                .PassengerNameRecord(pnrCodeGenerator.generateUniquePnrCodes())
                .bookingStatus(BookingStatus.PENDING)
                .build();

        primaryBooking = bookingRepository.save(primaryBooking);
        bookingRepository.flush();
        String approvalUrl = paymentService.paymentProcessings(totalFare, foundUser, primaryBooking, foundUser.getEmail(), bookTrainDTO.getPaymentMethod());
        if(approvalUrl == null || approvalUrl.isEmpty()){
            return new BookingResponse("Error: Payment processing failed, approval URL not generated.");
        }

        primaryBooking.setApprovalUrl(approvalUrl);
        bookingRepository.save(primaryBooking);
        List<OtherPassenger> savedAdditionalPassengers = otherPassengerService.bookTrainForOtherPassengers(bookTrainDTO,foundUser, fare, primaryBooking);
        return new BookingResponse(primaryBooking.getBookingId(),"Payment initiated. Please complete the payment using the provided URL.", approvalUrl);
    }



    public  BookingResponse confirmBooking(Long bookingId) {
        Booking primaryBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found for ID: " + bookingId));

        boolean isPaymentSuccessful = paymentService.verifyPayment(primaryBooking);
        if (!isPaymentSuccessful) {
            return new BookingResponse(
                    primaryBooking.getBookingId(),
                    "Error: Payment was not successful, seat will not be booked.",
                    0,
                    null,
                    primaryBooking.getTotalFareAmount(),
                    primaryBooking.getUser(),
                    primaryBooking.getBookingDate(),
                    null,
                    null
            );
        }
         Seat bookedSeat = seatService.bookSeat(primaryBooking.getTrainClass().getClassName(), primaryBooking.getSeatNumber());
        primaryBooking.setBookingStatus(BookingStatus.BOOKED);
        bookingRepository.save(primaryBooking);
        return getBookingResponse(primaryBooking.getUser(),bookedSeat,primaryBooking.getTotalFareAmount(),
                primaryBooking.getTotalFareAmount(),primaryBooking,null,primaryBooking.getBookingDate(), primaryBooking.getApprovalUrl());
    }

        private static BookingResponse getBookingResponse(User foundUser,Seat bookedSeat,Double primaryPassengerFare,Double totalFare,Booking primaryBooking,
                                                          List<OtherPassenger> savedAdditionalPassengers,LocalDateTime bookingDate,String approvalUrl) {

        return new BookingResponse(primaryBooking.getBookingId(),"Booking successful",bookedSeat.getSeatNumber(),
                primaryPassengerFare, totalFare, foundUser, bookingDate,null, approvalUrl
        );
    }







}


