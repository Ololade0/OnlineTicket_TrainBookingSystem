package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String message;
    private int bookedSeat;
    private Double fareAmount;
    private Double totalFareAmount;
    private User user; // Main user details
    private LocalDateTime bookingDate;


    private List<OtherPassenger> additionalPassengers; // List of additional passengers
    private String approvalUrl;

    public BookingResponse(Long bookingId, String message, int bookedSeat, Double fareAmount, Double totalFareAmount,
                           User user, LocalDateTime bookingDate, List<OtherPassenger> additionalPassengers, String approvalUrl) {
        this.bookingId = bookingId;
        this.message = message;
        this.bookedSeat = bookedSeat;
        this.fareAmount = fareAmount;
        this.totalFareAmount = totalFareAmount;
        this.user = user;
        this.bookingDate = bookingDate;
        this.additionalPassengers = additionalPassengers;
        this.approvalUrl = approvalUrl;
    }

    public BookingResponse(String s) {
        this.message = s;
    }
}