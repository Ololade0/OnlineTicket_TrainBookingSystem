package OnlineBookingSystem.OnlineBookingSystem.dto.request;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class FindScheduleDTO {
    private String arrivalStation;
    private String  departureStation;
    private LocalDate date;
    private Fare fare;
    private Long scheduleId;

}
