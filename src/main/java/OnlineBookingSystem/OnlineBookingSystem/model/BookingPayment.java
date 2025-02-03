package OnlineBookingSystem.OnlineBookingSystem.model;


import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentMethod;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@Entity(name = "payments")
@AllArgsConstructor
@NoArgsConstructor
public class BookingPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double totalPrice;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionReference;
    private LocalDateTime paymentDate;
    private String currency;
//    private String intent;
//    private String description;
    private String cancelUrl;
    private String successUrl;




    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}


