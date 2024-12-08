package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleResponse {
    private List<Schedule> schedules;
    private Fare fareDetails;
}
