package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class FindScheduleDTO {
    private String arrivalStation;
    private String  departureStation;
    private LocalDate date;
    private Fare fare;

}
