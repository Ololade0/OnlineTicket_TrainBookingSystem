package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;

import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TrainClassRequest {
    private String className;

    private Fare fare;


}
