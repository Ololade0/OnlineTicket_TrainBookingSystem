package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.service.StationService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/station")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;

    private final TrainClassService trainClassService;



    @PostMapping("/create-station")
    public ResponseEntity<?> createStation(@RequestBody Station createNewStation){
        Station newStation= stationService.createNewStation(createNewStation);
        return new ResponseEntity<>(newStation, HttpStatus.CREATED);
    }
    @GetMapping("/{stationId}")
    public ResponseEntity<?>findStationById(@PathVariable Long stationId){
        Station foundStation = stationService.findStationById(stationId);
        return new ResponseEntity<>(foundStation, HttpStatus.OK);
    }

    @PutMapping("/{stationId}")
    public ResponseEntity<?> updateStationById(@PathVariable Long stationId, @RequestBody Station station) {
        if (station == null) {
            return new ResponseEntity<>("Station details cannot be null", HttpStatus.BAD_REQUEST);
        }
        Station updatedStation = stationService.updateStation(station, stationId);
        return new ResponseEntity<>(updatedStation, HttpStatus.ACCEPTED);
    }



}
