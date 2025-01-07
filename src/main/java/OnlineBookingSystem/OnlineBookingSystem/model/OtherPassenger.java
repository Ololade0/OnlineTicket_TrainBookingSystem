//package OnlineBookingSystem.OnlineBookingSystem.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.List;
//
//
//@Setter
//@Getter
//@Builder
//
//@Entity(name = "passenger")
//@AllArgsConstructor
//@NoArgsConstructor
//
//
//
//public class OtherPassenger {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long  passengerId;
//    private String passengerName;
//    private String passengerEmail;
//    private String passengerIdentificationNumber;
//    private String passengerPhonumber;
//    private String passengerType;
//    private int seatNumber;
//
//    @JsonBackReference
//    @OneToMany(mappedBy = "otherPassenger", cascade = CascadeType.ALL)
//    private List<Booking> bookings;
//}
