package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

}