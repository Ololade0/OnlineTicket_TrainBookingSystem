package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
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
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookingPayment bookingPayment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus; // PENDING, PAID, CANCELLED


    private Double fareAmount;

    private String passengerType;

//    private PaymentStatus paymentStatus;


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
    @Builder
    public Booking(Long bookingId, LocalDateTime bookingDate, Double fareAmount, BookingStatus bookingStatus,
                   TrainClass trainClass, User user, Schedule schedule, List<Seat> seats, BookingPayment bookingPayment) {
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.fareAmount = fareAmount;
        this.bookingStatus = bookingStatus;
        this.trainClass = trainClass;
        this.user = user;
        this.schedule = schedule;
        this.seats = seats != null ? seats : new ArrayList<>(); // Ensure seats is not null
        this.bookingPayment = bookingPayment;
    }

}
