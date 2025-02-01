package OnlineBookingSystem.OnlineBookingSystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString(exclude = {"seats", "payment"})
@Entity(name = "bookings")
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDateTime bookingDate;

    @JsonManagedReference
    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "train_class_id")
    private TrainClass trainClass;

    @JsonManagedReference
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private BookingPayment bookingPayment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private Double fareAmount;

    private String passengerType;


    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OtherPassenger> otherPassengers = new ArrayList<>();

    public Booking() {
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        if (this.seats == null) {
            this.seats = new ArrayList<>();
        }
        this.seats.add(seat);
    }
}
