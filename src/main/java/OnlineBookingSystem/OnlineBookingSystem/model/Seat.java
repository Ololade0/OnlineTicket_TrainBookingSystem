package OnlineBookingSystem.OnlineBookingSystem.model;


import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@Entity(name = "seats")
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNumber;
//    private int totalSeat;
    @Embedded
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "train_class_id")
    @JsonBackReference
    private TrainClass trainClass;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}


