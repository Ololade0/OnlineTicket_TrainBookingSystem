package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.Route;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
@Setter
@Getter
public class UpdateScheduleDto {

    private Long scheduleId;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate departureDate;

    private LocalDate arrivalDate;
    private Duration duration;

    private ScheduleType scheduleType;
    private Route route;
}
