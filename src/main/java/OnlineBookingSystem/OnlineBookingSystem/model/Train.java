package OnlineBookingSystem.OnlineBookingSystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainClass> trainClasses = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Schedule> schedules;

    public void addTrainClass(TrainClass trainClass) {
        if (trainClasses == null) {
            trainClasses = new ArrayList<>();
        }
        trainClasses.add(trainClass);
        trainClass.setTrain(this);
    }
}
