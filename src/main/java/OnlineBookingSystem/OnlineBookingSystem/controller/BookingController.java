package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.BookingResponse;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidPassengerTypeException;
import OnlineBookingSystem.OnlineBookingSystem.service.BookingService;
import OnlineBookingSystem.OnlineBookingSystem.service.OtherPassengerService;
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
    private OtherPassengerService otherPassengerService;




    @PostMapping("/bookTrain")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookTrainDTO bookTrainDTO) {
        System.out.println("Incoming payload: " + bookTrainDTO);
        try {
            BookingResponse bookingResponse = bookingService.createBooking(bookTrainDTO);
            return ResponseEntity.ok(bookingResponse);
        } catch (Exception | InvalidPassengerTypeException e) {

            return ResponseEntity.badRequest().body(new BookingResponse("Error: " + e.getMessage()));
        }
    }



    @PostMapping("/book")
    public void deleteBooking() {

        otherPassengerService.deleteAll();
    }

}
