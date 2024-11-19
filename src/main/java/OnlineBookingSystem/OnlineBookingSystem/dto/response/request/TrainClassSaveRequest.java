package OnlineBookingSystem.OnlineBookingSystem.dto.response.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainClassSaveRequest {
    private List<TrainClassRequest> trainClassRequests;
    private GenerateSeatDTO generateSeatDTO;
}
