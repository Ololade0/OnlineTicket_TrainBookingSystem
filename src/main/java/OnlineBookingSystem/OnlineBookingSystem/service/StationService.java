package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import org.springframework.stereotype.Service;


public interface StationService {
    Station createNewStation(Station newStation);
}
