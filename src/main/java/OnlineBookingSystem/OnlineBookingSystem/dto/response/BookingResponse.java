package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long bookingId; // You can set this after creating a booking
    private String message;
    private int bookedSeats;
    private Double fareAmount;
    private Double totalFareAmount;
    private User user;
//    private List<PassengerDTO> passengerDTOList = new ArrayList<>();

    private String secondPassengerEmail;
    private String secondPassengerName;
    private String secondPassengerIdNumber;
    private String secondPassengerPhoneNumber;
    private int secondPassengerSeatNumber;
    private Double secondPassengerFareAmount;


    public BookingResponse(Long bookingId, String message, int bookedSeats, Double fareAmount, Double totalFareAmount, User user) {
        this.bookingId = bookingId;
        this.message = message;
        this.bookedSeats = bookedSeats;
        this.fareAmount = fareAmount;
        this.totalFareAmount = totalFareAmount;
        this.user = user;
    }


    public BookingResponse(String s) {
        this.message = s;
    }
}