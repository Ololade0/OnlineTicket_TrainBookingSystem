package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.IdentificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class SignUpUserResponse {
    private Long id;

    private String firstName;
    private String lastName;

    private String email;

    private GenderType gender;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String message;

    private String idNumber;



    ;


}
