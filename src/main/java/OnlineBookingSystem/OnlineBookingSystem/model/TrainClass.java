package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@ToString
@Entity(name = "trainclass")
@AllArgsConstructor
@NoArgsConstructor
public class TrainClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainClassId;

    private String className;
    @Embedded
    private Fare fare;

    @ManyToOne(optional = true)
    @JoinColumn(name = "train_id", nullable = true)
    private Train train;

    @OneToMany(mappedBy = "trainClass", cascade   = {
            CascadeType.PERSIST, CascadeType.REMOVE
    })
    private List<Seat> seats;

    // Getters and setters
}

