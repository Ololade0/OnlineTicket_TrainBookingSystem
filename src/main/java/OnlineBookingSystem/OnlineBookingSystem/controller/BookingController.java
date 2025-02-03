package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.service.BookingService;
import OnlineBookingSystem.OnlineBookingSystem.service.OtherPassengerService;
import OnlineBookingSystem.OnlineBookingSystem.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/book")
@Slf4j
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;




    @PostMapping("/bookTrain")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookTrainDTO bookTrainDTO) {
        try {
            BookingResponse bookingResponse = bookingService.createBooking(bookTrainDTO);
            return ResponseEntity.ok(bookingResponse);
        } catch (Exception | InvalidPassengerTypeException e) {

            return ResponseEntity.badRequest().body(new BookingResponse("Error: " + e.getMessage()));
        }
    }

//    @PostMapping("/bookTrains")
//    public ResponseEntity<BookingResponse> createBookings(@RequestBody BookTrainDTO bookTrainDTO) {
//        try {
//            BookingResponse bookingResponse = bookingService.createBooking(bookTrainDTO);
//            String approvalUrl = paymentService.paymentProcessings(bookingResponse.getTotalFareAmount(), bookTrainDTO.getPaymentMethod());
//            bookingResponse.setApprovalUrl(approvalUrl);
//
//            return ResponseEntity.ok(bookingResponse);
//        } catch (InvalidPassengerTypeException e) {
//            return ResponseEntity.badRequest().body(new BookingResponse("Error: " + e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookingResponse("Error: " + e.getMessage()));
//        }
//    }


}
