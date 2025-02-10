package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.OtherPassenger;
import OnlineBookingSystem.OnlineBookingSystem.model.Seat;
import OnlineBookingSystem.OnlineBookingSystem.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    private Long bookingId;
    private String message;
    private int bookedSeat;
    private Double fareAmount;
    private Double totalFareAmount;
    private User user;
    private LocalDateTime bookingDate;


    private List<OtherPassenger> additionalPassengers;
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

    public BookingResponse(String approvalUrl, String message) {
        this.approvalUrl = approvalUrl;
        this.message = message;
    }

    public BookingResponse(Long bookingId, String message, String approvalUrl) {
        this.bookingId = bookingId;
        this.message = message;
        this.approvalUrl = approvalUrl;
    }


}