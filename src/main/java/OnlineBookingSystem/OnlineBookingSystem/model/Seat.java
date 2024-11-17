package OnlineBookingSystem.OnlineBookingSystem.model;


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

    private String seatNumber;
    private String status;

    @ManyToOne
    @JoinColumn(name = "train_class_id", nullable = false)
    private TrainClass trainClass;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // Getters and setters
}


