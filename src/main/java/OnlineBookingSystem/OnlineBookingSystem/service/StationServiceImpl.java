package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.StationAlreadyExistException;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.repositories.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StationServiceImpl implements StationService{

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
