package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import OnlineBookingSystem.OnlineBookingSystem.service.Impl.TimetableResponse;
import OnlineBookingSystem.OnlineBookingSystem.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/schedule")
public class ScheduleController {
        @Autowired
        private ScheduleService scheduleService;

        @PostMapping("/createschedule")
        public ResponseEntity<?> createSchedules(
                @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  @RequestParam("trainId") Long trainId) {
            try {
                List<Schedule> schedules = scheduleService.createSchedules(date, trainId);
                return new ResponseEntity<>(schedules, HttpStatus.CREATED);
            } catch (InvalidScheduleException | ScheduleDetailsException e) {
                throw new RuntimeException(e);
            }
        }

    @PostMapping("/findall")
    public ResponseEntity<?> findAllSchedule() {
            List<Schedule> foundSchedule = scheduleService.findAllSchedule();
            return ResponseEntity.ok(foundSchedule);
    }
//    @GetMapping("/timetable")
//    public ResponseEntity<List<TimetableResponse>> getTimetable() {
//        List<TimetableResponse> timetable = scheduleService.timetableForMovt();
//        return ResponseEntity.ok(timetable);
//    }


    @PostMapping("/find")
    public ResponseEntity<?> findSchedule(@RequestBody FindScheduleDTO findScheduleDTO) {
        try {
            ScheduleResponse scheduleResponse = scheduleService.findSchedule(findScheduleDTO);
            return ResponseEntity.ok(scheduleResponse);
        } catch (ScheduleCannotBeFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    }



