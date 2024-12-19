package OnlineBookingSystem.OnlineBookingSystem.dto.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.Route;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Setter
@Getter
public class CreateScheduleDTO {
    private String arrivalStationName;
    private String departureStationName;
    private Long trainClassId;
    private Long trainId;

    private LocalTime departureTime;

    private LocalTime arrivalTime;

    private LocalDate departureDate;

    private LocalDate arrivalDate;
    private Duration duration;
    private ScheduleType scheduleType;

    private Route route;
    private Fare fare;
}
