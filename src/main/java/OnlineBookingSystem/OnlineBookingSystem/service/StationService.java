package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface StationService {
    Station createNewStation(Station newStation);
    Station findStationById(Long stationId);
    Station findStationByName(String stationName);
    Station updateStation(Station station, Long stationId);
}
