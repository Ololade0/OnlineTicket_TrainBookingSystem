package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class CreateScheduleDTO {
    private LocalDate date;
    private Long trainId;
    private String stationName;

}
