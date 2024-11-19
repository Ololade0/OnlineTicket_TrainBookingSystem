package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationAlreadyExistException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.repositories.StationRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.StationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public Station createNewStation(Station newStation) {
        verifyStationName(newStation.getStationName());
        verifyStationCode(newStation.getStationCode());
        Station station = Station.builder()
                .stationCode(newStation.getStationCode())
                .stationName(newStation.getStationName())
                .build();
        return stationRepository.save(station);
    }

    @Override
    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationCannotBeFoundException("Station with ID " + stationId + " cannot be found"));
    }


    @Transactional
    public Station updateStation(Station station, Long stationId) {
        Optional<Station> foundStation = stationRepository.findById(stationId);
        if (foundStation.isPresent()) {
            Station existingStation = foundStation.get();
            existingStation.setStationCode(station.getStationCode());
            existingStation.setStationName(station.getStationName());
            return stationRepository.save(existingStation);
        }
        throw new StationCannotBeFoundException("Station with ID " + stationId + " cannot be found");
    }


    private void verifyStationCode(String stationCode) {
        if (stationRepository.existsByStationCode(stationCode)) {
            throw new StationAlreadyExistException("Station with code " + stationCode + " already exist");

        }

    }


    private void verifyStationName(String station){
            if(stationRepository.existsByStationName(station)){
                throw new StationAlreadyExistException("Station with name " + station + " already exist");
            }



    }
}
