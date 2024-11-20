package OnlineBookingSystem.OnlineBookingSystem.controller;


import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/trainsclass")
public class TrainClassController {

    @Autowired
    private TrainClassService trainClassService;


//
    @PostMapping("/train-classes")
    public ResponseEntity<?> saveTrainClasses(
            @RequestBody TrainClass trainClass,
            @RequestParam int startSeat,
            @RequestParam int endSeat
            ) {
        TrainClass trainClasses = trainClassService.saveTrainClasses(trainClass, startSeat, endSeat);
        return ResponseEntity.ok(trainClasses);
    }


    }












//    @GetMapping("/{trainClassId}")
//    public ResponseEntity<?>findTrainClassById(@PathVariable Long trainClassId) throws TrainClassCannotBeFoundException {
//       return new ResponseEntity<>(trainClassService.findTrainClassById(trainClassId), HttpStatus.OK);
//    }
//
//
//    @GetMapping("/findAll")
//    public ResponseEntity<?>findAllTrain(){
//        return new ResponseEntity<>(trainClassService.findAllTrainClass(), HttpStatus.OK);
//    }
////    @GetMapping("findAll")
//    public ResponseEntity<?>findTrainAll(){
//        return new ResponseEntity<>(trainClassService., HttpStatus.OK);
//    }




