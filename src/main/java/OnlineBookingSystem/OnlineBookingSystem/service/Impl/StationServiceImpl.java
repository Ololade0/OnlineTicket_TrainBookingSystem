package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationAlreadyExistException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.repositories.StationRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.StationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public Station createNewStation(Station newStation) {
        verifyStation(newStation.getStationName(), newStation.getStationCode());
        Station station = Station.builder()
                .stationCode(newStation.getStationCode())
                .stationName(newStation.getStationName())
                .build();
        return stationRepository.save(station);
    }

    @Override
    public Station findStation(String station) {
        return null;
    }

    @Override
    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationCannotBeFoundException("Station with ID " + stationId + " cannot be found"));
    }

    @Override
    public Optional<Station> findStationByName(String stationName) {
        return Optional.ofNullable(stationRepository.findByStationName(stationName).orElseThrow(()
                -> new StationCannotBeFoundException
                ("Station with Name " + stationName + " cannot be found")));
    }

//    Optional<Station> findStationByName(String name) {
//        List<Station> stations = stationRepository.findByName(name);
//        if (stations.size() > 1) {
//            throw new IllegalStateException("Multiple stations found with the name: " + name);
//        }
//        return stations.stream().findFirst();
//    }


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

    @Override
    public List<Station> findAllStation() {
        return stationRepository.findAll();
    }

//    @Override
//    public List<Station> findStationsByName(String stationName) {
//       return stationRepository.findAll();
//    }


    private void verifyStation(String stationCode, String stationName) {
        if (stationRepository.existsByStationCode(stationCode)) {
            throw new StationAlreadyExistException("Station with code " + stationCode + " already exists");
        }
        if (stationRepository.existsByStationName(stationName)) {
            throw new StationAlreadyExistException("Station with name " + stationName + " already exists");
        }



    }
}
