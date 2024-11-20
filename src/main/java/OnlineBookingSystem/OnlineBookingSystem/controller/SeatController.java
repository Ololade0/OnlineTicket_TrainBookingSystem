//package OnlineBookingSystem.OnlineBookingSystem.controller;
//
//import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
//import OnlineBookingSystem.OnlineBookingSystem.service.SeatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("api/seat")
//@RequiredArgsConstructor
//public class SeatController {
//
//    private final SeatService seatService;
//@PostMapping("/create-seat")
//    public ResponseEntity<?> generateSeat(
//                                          @RequestParam int startSeat,
//                                          @RequestParam int endSeat){
//        List<Seat> generatedSeat =seatService.generateSeats(startSeat,endSeat);
//        return new ResponseEntity<>(generatedSeat, HttpStatus.CREATED);
//
//    }
//}
