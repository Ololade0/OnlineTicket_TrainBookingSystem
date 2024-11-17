package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    private Long stationId;
    @NotBlank(message = "Station Name cannot be blank")
    private String stationName;

    @NotBlank(message = "Station Code cannot be blank")
    private String stationCode;

    @ManyToMany(mappedBy = "stations")
    private List<Schedule> schedules;

    // Getters and setters
}

