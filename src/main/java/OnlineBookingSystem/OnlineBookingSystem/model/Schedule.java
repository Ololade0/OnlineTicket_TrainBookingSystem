package OnlineBookingSystem.OnlineBookingSystem.model;


import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Setter
@Getter
@Builder
@ToString
@Entity(name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends AuditBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate departureDate;

    private LocalDate arrivalDate;
    private Duration duration;
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;



    @JsonBackReference  // Prevents recursion when serializing Train object
    @ManyToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToMany
    @JoinTable(
            name = "schedule_station",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "station_id")
    )
    private List<Station> stations;

    // Getters and setters
}

