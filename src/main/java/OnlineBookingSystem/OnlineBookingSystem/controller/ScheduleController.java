package OnlineBookingSystem.OnlineBookingSystem.controller;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.BookTrainDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.CreateScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Booking;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import OnlineBookingSystem.OnlineBookingSystem.service.BookingService;
import OnlineBookingSystem.OnlineBookingSystem.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("api/schedule")
@Slf4j
public class ScheduleController {
        @Autowired
        private ScheduleService scheduleService;
    @Autowired
    private BookingService bookingService;


    @PostMapping("create-schedule")
    public ResponseEntity<?> createSchedules(@RequestBody CreateScheduleDTO createScheduleDTO) throws InvalidScheduleException {
        Schedule createdSchedule = scheduleService.createSchedules(createScheduleDTO);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);

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

    @PostMapping("/bookTrain")
    public ResponseEntity<Booking> createBooking(
            @RequestBody BookTrainDTO bookTrainDTO,
            @RequestParam Long scheduleId,
            @RequestParam Long stationId) {

        // Create a FindScheduleDTO based on the provided scheduleId
        FindScheduleDTO findScheduleDTO = FindScheduleDTO.builder()
                .scheduleId(scheduleId)
                .build();

        // Call the service to create a booking
        Booking booking = bookingService.createBooking(bookTrainDTO, findScheduleDTO);
        return ResponseEntity.ok(booking);
    }

    }



