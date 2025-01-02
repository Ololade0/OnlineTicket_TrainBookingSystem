package OnlineBookingSystem.OnlineBookingSystem.dto.request;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindScheduleDTO {
    private String arrivalStation;
    private String  departureStation;
    private LocalDate date;
    private Fare fare;
    private Long scheduleId;

}
