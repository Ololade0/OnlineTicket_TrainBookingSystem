package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

      public List<Schedule> createSchedules(LocalDate date, Long trainId) throws InvalidScheduleException, ScheduleDetailsException;
//    Schedule createSchedule();
//
//    Schedule findSchedule(Long schedule);
}
