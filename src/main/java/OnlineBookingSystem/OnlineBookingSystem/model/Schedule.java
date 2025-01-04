package OnlineBookingSystem.OnlineBookingSystem.model;


import OnlineBookingSystem.OnlineBookingSystem.model.enums.Route;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
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
    @JsonFormat(pattern = "HH:mm:ss")

    private LocalTime departureTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;
    private String distance;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Enumerated(EnumType.STRING)
    private Route route;

    @Embedded
    private Fare fare;



    @JsonBackReference
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

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Booking> bookings;

}

