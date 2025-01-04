package OnlineBookingSystem.OnlineBookingSystem.controller;


import OnlineBookingSystem.OnlineBookingSystem.dto.request.AddTrainClassToTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;

import OnlineBookingSystem.OnlineBookingSystem.service.TrainService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/train/")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;
    @Operation(
            summary = "Create a new train",
            description = "This API allows users to add a new train with specific classes and details.",
            tags = {"Post"}
    )
    @PostMapping(value = "/create")
    public ResponseEntity<?> createTrain(@RequestBody AddTrainClassToTrainDTO addTrainClassToTrainDTO) {
        Train train = trainService.createNewTrains(addTrainClassToTrainDTO);
        return new ResponseEntity<>(train, HttpStatus.CREATED);
    }

    @GetMapping("/{trainId}")
    public ResponseEntity<?> findTrainById(@PathVariable Long trainId) {
        Train findTrain = trainService.findTrainById(trainId);

        return new ResponseEntity<>(findTrain, HttpStatus.CREATED);
    }
}
