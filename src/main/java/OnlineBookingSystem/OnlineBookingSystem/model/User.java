package OnlineBookingSystem.OnlineBookingSystem.model;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Builder
@ToString


@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AuditBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Kindly enter a valid email")
    private String email;
//    @NotNull(message = "Kindly choose your gender")
    private GenderType gender;
    private String password;
    private String confirmPassword;
    @NotBlank(message = "phonenumber must not be blank")
    private String phoneNumber;
    private LocalDate DateOfBirth;
//    @Size(min = 10, max = 15, message = "ID number must be between 10 and 15 characters")
    private String idNumber;
//    @NotNull(message = "Kindly choose mode of identification")
    private IdentificationType identificationType;



    @JsonIgnore()
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roleSet = new HashSet<>();




}


