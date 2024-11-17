package OnlineBookingSystem.OnlineBookingSystem.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@ToString
@Entity(name = "schedules")
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureTime;
    private String arrivalTime;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
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

