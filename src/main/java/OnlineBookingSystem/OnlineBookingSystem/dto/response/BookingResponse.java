package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String message;
    private int bookedSeat;
    private Double fareAmount;
    private Double totalFareAmount;
    private User user; // Main user details
    private List<OtherPassenger> additionalPassengers; // List of additional passengers

    public BookingResponse(String message, Long bookingId, int bookedSeat, Double fareAmount, Double totalFareAmount, User user, List<OtherPassenger> additionalPassengers) {
       this.message = message;
        this.bookingId = bookingId;
        this.bookedSeat = bookedSeat;
        this.fareAmount = fareAmount;
        this.totalFareAmount = totalFareAmount;
        this.user = user;
        this.additionalPassengers = additionalPassengers; 
    }

    public BookingResponse(String message) {
        this.message = message;
    }

    public BookingResponse(String s, Long bookingId, int seatNumber, Double selectedFareFirst, Double totalFare, User foundUser) {
    }
}