package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Fare {
    private int adult;
    private int minor;
}
