package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.service.DistanceCalculatorService;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/distance")
public class DistanceController {

    @Autowired
    private DistanceCalculatorService distanceCalculatorService;

    public DistanceController(DistanceCalculatorService distanceCalculatorService) {
        this.distanceCalculatorService = distanceCalculatorService;
    }

    @GetMapping("/distance")
    public ResponseEntity<?>  getDistance() {
        return new ResponseEntity<>(distanceCalculatorService.calculateDistance(), HttpStatus.OK);
    }
}
