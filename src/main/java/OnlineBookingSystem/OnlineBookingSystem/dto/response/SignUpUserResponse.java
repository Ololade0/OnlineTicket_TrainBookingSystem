package OnlineBookingSystem.OnlineBookingSystem.dto.response;

import OnlineBookingSystem.OnlineBookingSystem.model.enums.GenderType;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class SignUpUserResponse {
    private Long id;
       private String name;
    private String email;
    private GenderType gender;
    private String message;



    ;


}
