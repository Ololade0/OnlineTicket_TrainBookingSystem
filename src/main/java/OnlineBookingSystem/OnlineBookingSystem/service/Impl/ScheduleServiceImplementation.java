package OnlineBookingSystem.OnlineBookingSystem.service.Impl;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.CreateScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.UpdateScheduleDto;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.*;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.ScheduleRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class ScheduleServiceImplementation implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImplementation.class);

    private final ScheduleRepository scheduleRepository;
    private final StationService stationService;
    private final  TrainClassService trainClassService;
    private final TrainService trainService;

    private final DistanceCalculatorService distanceCalculatorService;




    @Override
    public Schedule createSchedules(CreateScheduleDTO createSceduleDTO) {

        Optional<Station> arrivalStation = stationService.findStationByName(createSceduleDTO.getArrivalStationName());
        Optional<Station> departureStation = stationService.findStationByName(createSceduleDTO.getDepartureStationName());
        Train train = trainService.findTrainById(createSceduleDTO.getTrainId());
        TrainClass trainClass = trainClassService.findTrainClassById(createSceduleDTO.getTrainClassId());

        if (trainClass == null || trainClass.getFare() == null) {
            throw new IllegalArgumentException("Fare cannot be null for the given train class");
        }


        if (arrivalStation.isEmpty() || departureStation.isEmpty()) {
            throw new StationCannotBeFoundException("Arrival station or departure station cannot be null");
        }

        List<Station> stations = new ArrayList<>();
        stations.add(new Station(arrivalStation.get().getStationId(), arrivalStation.get().getStationName(), arrivalStation.get().getStationCode(), "arrival"));
        stations.add(new Station(departureStation.get().getStationId(), departureStation.get().getStationName(), departureStation.get().getStationCode(),  "departure"));
        Schedule schedule = Schedule.builder()
                .route(createSceduleDTO.getRoute())
                .scheduleType(createSceduleDTO.getScheduleType())
                .arrivalTime(createSceduleDTO.getArrivalTime())
                .departureTime(createSceduleDTO.getDepartureTime())
                .arrivalDate(createSceduleDTO.getArrivalDate())
                .departureDate(createSceduleDTO.getDepartureDate())
                .fare(trainClass.getFare())
                .train(train)
                .distance(distanceCalculatorService.calculateDistance())
                .stations(stations)
                .build();
//        validateScheduleTimetable(departureStation.get(),arrivalStation.get(),createSceduleDTO.getScheduleType(),
//                createSceduleDTO.getArrivalDate(), createSceduleDTO.getDepartureTime(), createSceduleDTO.getArrivalTime());

        Duration duration = Duration.between(createSceduleDTO.getDepartureTime(), createSceduleDTO.getArrivalTime());
        schedule.setDuration(duration);

        return scheduleRepository.save(schedule);
    }

    private static void validateScheduleTimetable(Station departureStations, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, LocalTime arrivalTime)  {
        if (departureStations == null || arrivalStation == null || scheduleType == null
                || date == null || departureTime == null) {
            throw new ScheduleDetailsException("All parameters must be provided and cannot be null.");
        }
        if (arrivalTime.isBefore(departureTime)) {
            throw new ScheduleDetailsException("Arrival time cannot be before departure time.");
        }
        LocalDate currentDate = LocalDate.now();
        if (date.isEqual(currentDate) || date.isBefore(currentDate)) {
            throw new ScheduleDetailsException("The departure date must be at least 2 days in advance. or you cannot schedule a train for a past date ");
        }

        LocalDate minScheduleDate = currentDate.plusDays(2);
        if (date.isBefore(minScheduleDate)) {
            throw new ScheduleDetailsException("Schedules can only be created for at least 2 days in advance.");
        }

        LocalDate maxScheduleDate = currentDate.plusDays(2);
        if (date.isAfter(maxScheduleDate)) {
            throw new InvalidScheduleException("Schedules can only be created for up to 2 days in advance.");
        }
    }



    @Override
    public ScheduleResponse findSchedule(FindScheduleDTO findScheduleDTO) {
        validateStationNames(findScheduleDTO);
        Optional<Station> arrivalStation = stationService.findStationByName(findScheduleDTO.getArrivalStation());
        Optional<Station> departureStation = stationService.findStationByName(findScheduleDTO.getDepartureStation());

        if (arrivalStation.isEmpty() || departureStation.isEmpty()) {
            throw new ScheduleCannotBeFoundException("Invalid station names provided.");
        }

        List<Schedule> schedules = scheduleRepository.findSchedule(
                findScheduleDTO.getArrivalStation(),
                findScheduleDTO.getDepartureStation(),
                findScheduleDTO.getDate()
        );

        if (schedules.isEmpty()) {
            throw new ScheduleCannotBeFoundException("No schedules found for the given criteria.");
        }

        return scheduleResponses(schedules, findScheduleDTO.getFare());
    }

    private void validateStationNames(FindScheduleDTO findScheduleDTO) {
        if (findScheduleDTO.getArrivalStation() == null || findScheduleDTO.getDepartureStation() == null) {
            throw new IllegalArgumentException("Arrival and departure station names must not be null.");
        }
    }
    private ScheduleResponse scheduleResponses(List<Schedule> scheduleList, Fare fare){
      return new ScheduleResponse(scheduleList, fare);

    }


    @Override
    public Schedule updateSchedule(UpdateScheduleDto updateScheduleDto) {
        Schedule foundSchedule = scheduleRepository.findById(updateScheduleDto.getScheduleId()).orElseThrow(()
                -> new ScheduleCannotBeFoundException("Schedule with ID" + updateScheduleDto.getScheduleId() + " cannot be found"));

        foundSchedule.setArrivalDate(updateScheduleDto.getArrivalDate());

        return foundSchedule;
    }

    @Override
    public List<Schedule> findAllSchedule() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule findByScheduleId(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(()-> new ScheduleCannotBeFoundException("Schedule does not exist"));
    }
}




