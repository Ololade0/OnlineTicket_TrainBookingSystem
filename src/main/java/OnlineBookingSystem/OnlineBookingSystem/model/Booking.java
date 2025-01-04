package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Builder
@ToString(exclude = {"seats", "payment"})
@Entity(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "train_class_id")
    private TrainClass trainClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private Double fareAmount;



}
