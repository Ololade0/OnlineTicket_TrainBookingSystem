package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.Role;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.RoleType;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignUpUserResponse {
    private Long userId;
//    private Long roleId;
    private String name;
    private String email;
    private GenderType gender;
    private String message;
}
