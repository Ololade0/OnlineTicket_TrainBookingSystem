package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;


@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "other")
public class OtherPassenger {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;

        private String email;

        private GenderType gender;

        private String phoneNumber;
        private String idNumber;
        private String passengerType;
        private int seatNumber;
        private IdentificationType identificationType;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "booking_id", nullable = false)
        private Booking booking;








}


