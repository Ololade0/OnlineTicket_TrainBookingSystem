package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.context.request.FacesRequestAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@ToString(exclude = {"seats", "BookingPayment"})

@Entity(name = "bookings")
@AllArgsConstructor

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private LocalDateTime bookingDate;

    private String PassengerNameRecord;

    private LocalDateTime travelDate;
    private Double totalFareAmount;

    private String passengerType;
    private int seatNumber;

    private String approvalUrl;
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
    @Column(name = "booking_status", nullable = false, length = 20)
    private BookingStatus bookingStatus;



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

    public void updateStatus(BookingStatus newStatus) {
        this.bookingStatus = newStatus;
    }



}
