package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.TrainClassDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingResponse {

    private Long bookingId;
//    private User user;
//    private TrainClass trainClass;
//    private Long bookedSeat;
//    private Schedule schedule;
//    private String message;
//    private Double fareAmount;
private String message;
    private int bookedSeat;
    private Double fareAmount;
//    private TrainClassDTO trainClass;
    private User user;
//    private ScheduleDTO schedule;
//    private Fare fare;

}
