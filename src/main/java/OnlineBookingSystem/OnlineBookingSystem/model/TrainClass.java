package OnlineBookingSystem.OnlineBookingSystem.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
//
    @Embedded
    private Fare fare;


    @JsonBackReference("train-trainClass")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private Train train;

    @JsonManagedReference("trainClass-seats")
    @OneToMany(mappedBy = "trainClass", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Seat> seats = new ArrayList<>();

   @JsonBackReference("trainClass-bookings")
//    @JsonIgnore
    @OneToMany(mappedBy = "trainClass", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Booking> bookings;


    private int totalSeat;

    public TrainClass(Long trainClassId, String className, Object o, int i) {
    }
}