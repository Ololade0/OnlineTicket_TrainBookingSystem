package OnlineBookingSystem.OnlineBookingSystem.dto.response;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    // Existing attributes...
    private long bookingId;
    private String message;
    private String bookedSeats;
    private double fareAmount;
    private User user;
    private String secondPassengerEmail;
    private String secondPassengerName;
    private String secondPassengerIdNumber;
    private String secondPassengerPhoneNumber;
    private int secondPassengerSeatNumber;
    private double SecondPassengerFareAmount;

}

    // Getters and Setters
