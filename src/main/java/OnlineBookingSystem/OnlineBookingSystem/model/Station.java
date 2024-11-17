package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;@Setter
@Getter
@Builder
@ToString
@Entity(name = "stations")
@AllArgsConstructor
@NoArgsConstructor
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;
    private String stationCode;

    @ManyToMany(mappedBy = "stations")
    private List<Schedule> schedules;

    // Getters and setters
}

