package OnlineBookingSystem.OnlineBookingSystem.model;


import OnlineBookingSystem.OnlineBookingSystem.model.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "train_class_id")
    private TrainClass trainClass;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}


