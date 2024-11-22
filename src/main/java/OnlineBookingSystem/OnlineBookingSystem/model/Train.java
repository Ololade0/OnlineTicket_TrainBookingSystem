package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@Entity(name = "trains")
@AllArgsConstructor
@NoArgsConstructor
public class Train extends AuditBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainClass> trainClasses = new ArrayList<>();

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

    // Getters and setters
}
