package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.*;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
import OnlineBookingSystem.OnlineBookingSystem.repositories.BookingRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import OnlineBookingSystem.OnlineBookingSystem.utils.PnrCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
@Override
    public BookingResponse createBooking(BookTrainDTO bookTrainDTO) throws InvalidPassengerTypeException, IOException, InterruptedException {
        log.debug("Starting createBooking for user: {}", bookTrainDTO.getUserEmail());

        User foundUser = userService.findUserByEmail(bookTrainDTO.getUserEmail());
        TrainClass foundTrainClass = trainClassService.findTrainClassByName(bookTrainDTO.getTrainClassName());

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
            throw new PaymentProcessingException("Error: Payment processing failed, approval URL not generated.");
        }
         primaryBooking.setApprovalUrl(approvalUrl);
        bookingRepository.save(primaryBooking);

        List<OtherPassenger> savedAdditionalPassengers = otherPassengerService.bookTrainForOtherPassengers(bookTrainDTO,foundUser, fare, primaryBooking);
        return new BookingResponse(primaryBooking.getBookingId(),"Payment initiated. Please complete the payment using the provided URL.", approvalUrl);
    }


@Override
    public  BookingResponse confirmBooking(Long bookingId) {
        Booking primaryBooking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingCannotBeFoundException("Booking not found for ID: " + bookingId));

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

    @Override
    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    private static BookingResponse getBookingResponse(User foundUser,Seat bookedSeat,Double primaryPassengerFare,Double totalFare,Booking primaryBooking,
                                                          List<OtherPassenger> savedAdditionalPassengers,LocalDateTime bookingDate,String approvalUrl) {
        Integer seatNumber = (bookedSeat != null) ? bookedSeat.getSeatNumber() : null;


        return new BookingResponse(primaryBooking.getBookingId(),"Booking successful",seatNumber,
                primaryPassengerFare, totalFare, foundUser, bookingDate,null, approvalUrl
        );
    }







}


