package OnlineBookingSystem.OnlineBookingSystem.service;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.CreateScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;

import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.UpdateScheduleDto;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

//      List<Schedule> createSchedules(LocalDate date, Long trainId) throws InvalidScheduleException, ScheduleDetailsException;
    Schedule createSchedules(CreateScheduleDTO createScheduleDTO) throws InvalidScheduleException, ScheduleDetailsException;
      ScheduleResponse findSchedule(FindScheduleDTO findScheduleDTO);

      Schedule updateSchedule(UpdateScheduleDto updateScheduleDto);

    List<Schedule> findAllSchedule();
//    List<TimetableResponse> timetableForMovt();
}
