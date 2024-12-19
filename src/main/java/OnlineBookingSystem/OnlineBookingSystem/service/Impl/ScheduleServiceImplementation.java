package OnlineBookingSystem.OnlineBookingSystem.service.Impl;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.CreateScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.request.UpdateScheduleDto;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.*;
import OnlineBookingSystem.OnlineBookingSystem.model.*;
import OnlineBookingSystem.OnlineBookingSystem.repositories.ScheduleRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.ScheduleService;
import OnlineBookingSystem.OnlineBookingSystem.service.StationService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainClassService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final TrainRepository trainRepository;





//    @Transactional
//    public List<Schedule> createSchedules(LocalDate date, Long trainId) throws InvalidScheduleException, ScheduleDetailsException {
//        List<Schedule> scheduleList = new ArrayList<>();
//        logger.info("Creating schedules for date: {}", date);
//
//
//        Train train = trainService.findTrainById(trainId);
////                .orElseThrow(() -> new ScheduleDetailsException("Train not found in the database."));
//
//        TrainTimetable(date, scheduleList, train);
//
//        if (scheduleList.isEmpty()) {
//            logger.warn("No schedules created for date: {}", date);
//            return scheduleList;
//        }
//
//        logger.info("Number of schedules to save: {}", scheduleList.size());
//        try {
//            return scheduleRepository.saveAll(scheduleList);
//        } catch (Exception e) {
//            logger.error("Error saving schedules: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to save schedules", e);
//        }
//    }
//
//    private void TrainTimetable(LocalDate date, List<Schedule> scheduleList, Train train) throws InvalidScheduleException, ScheduleDetailsException {
//        List<Optional<Station>> stations = List.of(
//                stationService.findStationByName("AGEGE"),
//                stationService.findStationByName("MONIYA"),
//                stationService.findStationByName("AGBADO"),
//                stationService.findStationByName("PAPALANTO"),
//                stationService.findStationByName("ABEOKUTA"),
//                stationService.findStationByName("OMI_ADIO"),
//                stationService.findStationByName("EBUTTE")
//        );
//
//        if (stations.stream().anyMatch(Optional::isEmpty)) {
//            logger.error("One or more stations are missing in the database.");
//            throw new ScheduleDetailsException("Required stations are not available in the database.");
//        }
//
//        Station agegeStation = stations.get(0).get();
//        Station moniyaStation = stations.get(1).get();
//        Station agbadoStation = stations.get(2).get();
//        Station papalantoStation = stations.get(3).get();
//        Station abeokutaStation = stations.get(4).get();
//        Station omiAdioStation = stations.get(5).get();
//        Station ebuteMettaStation = stations.get(6).get();
//
//        createMorningSchedules(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
//        createAfternoonSchedules(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
//    }
//
//    private void createMorningSchedules(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
//                                        Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) throws InvalidScheduleException {
//        scheduleList.add(createSchedule(moniyaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 0), train));
//        scheduleList.add(createSchedule(omiAdioStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 20), train));
//        scheduleList.add(createSchedule(abeokutaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 59), train));
//        scheduleList.add(createSchedule(papalantoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 23), train));
//        scheduleList.add(createSchedule(agbadoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 48), train));
//        scheduleList.add(createSchedule(agegeStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(10, 3), train));
//    }
//
//    private void createAfternoonSchedules(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
//                                          Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) throws InvalidScheduleException {
//        scheduleList.add(createSchedule(ebuteMettaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 0), train));
//        scheduleList.add(createSchedule(agegeStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 28), train));
//        scheduleList.add(createSchedule(agbadoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 43), train));
//        scheduleList.add(createSchedule(papalantoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 9), train));
//        scheduleList.add(createSchedule(abeokutaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 35), train));
//        scheduleList.add(createSchedule(omiAdioStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(18, 11), train));
//    }
//
//    private Schedule createSchedule(Station departureStation, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, Train train) throws InvalidScheduleException {
//
//        LocalTime arrivalTime = departureTime.plusHours(1).plusMinutes(45);
//        validateScheduleTimetable(departureStation,arrivalStation,scheduleType,date,departureTime, arrivalTime);
//        return Schedule.builder()
//                .departureTime(departureTime)
//                .arrivalTime(arrivalTime)
//                .departureDate(date)
//                .arrivalDate(date)
//                .duration(Duration.between(departureTime, arrivalTime))
//                .stations(List.of(departureStation, arrivalStation))
//                .scheduleType(scheduleType)
//                .train(train)
//                .build();
//    }
//
//
//    private static void validateScheduleTimetable(Station departureStations, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, LocalTime arrivalTime) throws ScheduleDetailsException, InvalidScheduleException {
//        if (departureStations == null || arrivalStation == null || scheduleType == null
//                || date == null || departureTime == null) {
//            throw new ScheduleDetailsException("All parameters must be provided and cannot be null.");
//        }
//        if (arrivalTime.isBefore(departureTime)) {
//            throw new ScheduleDetailsException("Arrival time cannot be before departure time.");
//        }
//        LocalDate currentDate = LocalDate.now();
//        if (date.isEqual(currentDate) || date.isBefore(currentDate)) {
//            throw new ScheduleDetailsException("The departure date must be at least 2 days in advance. or you cannot schedule a train for a past date ");
//        }
//
//        LocalDate minScheduleDate = currentDate.plusDays(2);
//        if (date.isBefore(minScheduleDate)) {
//            throw new ScheduleDetailsException("Schedules can only be created for at least 2 days in advance.");
//        }
//
////        LocalDate maxScheduleDate = currentDate.plusDays(2);
////        if (date.isAfter(maxScheduleDate)) {
////            throw new InvalidScheduleException("Schedules can only be created for up to 2 days in advance.");
////
////        }
//
//    }


//    @Override
//    public Schedule createSchedules(CreateScheduleDTO createSceduleDTO) {
//        Optional<Station> arrivalstation = stationService.findStationByName(createSceduleDTO.getArrivalStationName());
//        Optional<Station> departureStation = stationService.findStationByName(createSceduleDTO.getDepartureStationName());
//        Train train = trainService.findTrainById(createSceduleDTO.getTrainId());
//        TrainClass trainClass =  trainClassService.findTrainClassById(createSceduleDTO.getTrainClassId());
//       Fare fare = trainClass.getFare();
//        if (trainClass == null || trainClass.getFare() == null) {
//            throw new IllegalArgumentException("Fare cannot be null for the given train class");
//        }
//
//        if (arrivalstation.isEmpty() && departureStation.isEmpty()) {
//            throw new StationCannotBeFoundException("Arrival station or departure station cannot be null");
//        }
//
//
//        else {
//            Schedule schedule = Schedule.builder()
//                    .route(createSceduleDTO.getRoute())
//                    .scheduleType(createSceduleDTO.getScheduleType())
//                    .arrivalTime(createSceduleDTO.getArrivalTime())
//                    .departureTime(createSceduleDTO.getDepartureTime())
//                    .arrivalDate(createSceduleDTO.getArrivalDate())
//                    .departureDate(createSceduleDTO.getDepartureDate())
//                    .fare(fare)
//                    .train(train)
//                    .stations(Arrays.asList(arrivalstation.get(), departureStation.get()))
//
//                    .build();
//            Duration duration = Duration.between(createSceduleDTO.getArrivalTime(), createSceduleDTO.getDepartureTime());
//            schedule.setDuration(duration);
//            return scheduleRepository.save(schedule);
//        }
//
//    }

    @Override
    public Schedule createSchedules(CreateScheduleDTO createSceduleDTO) {
        Optional<Station> arrivalStation = stationService.findStationByName(createSceduleDTO.getArrivalStationName());
        Optional<Station> departureStation = stationService.findStationByName(createSceduleDTO.getDepartureStationName());
        Train train = trainService.findTrainById(createSceduleDTO.getTrainId());
        TrainClass trainClass = trainClassService.findTrainClassById(createSceduleDTO.getTrainClassId());

        // Validate fare
        if (trainClass == null || trainClass.getFare() == null) {
            throw new IllegalArgumentException("Fare cannot be null for the given train class");
        }

        // Validate stations
        if (arrivalStation.isEmpty() || departureStation.isEmpty()) {
            throw new StationCannotBeFoundException("Arrival station or departure station cannot be null");
        }

        // Create a list of stations with designations
        List<Station> stations = new ArrayList<>();
        stations.add(new Station(arrivalStation.get().getStationId(), arrivalStation.get().getStationName(), arrivalStation.get().getStationCode(), "arrival"));
        stations.add(new Station(departureStation.get().getStationId(), arrivalStation.get().getStationName(), arrivalStation.get().getStationCode(),  "departure"));

        // Build the schedule
        Schedule schedule = Schedule.builder()
                .route(createSceduleDTO.getRoute())
                .scheduleType(createSceduleDTO.getScheduleType())
                .arrivalTime(createSceduleDTO.getArrivalTime())
                .departureTime(createSceduleDTO.getDepartureTime())
                .arrivalDate(createSceduleDTO.getArrivalDate())
                .departureDate(createSceduleDTO.getDepartureDate())
                .fare(trainClass.getFare())
                .train(train)
                .stations(stations)
                .build();

        // Set duration
        Duration duration = Duration.between(createSceduleDTO.getDepartureTime(), createSceduleDTO.getArrivalTime());
        schedule.setDuration(duration);

        return scheduleRepository.save(schedule);
    }

    @Override
    public ScheduleResponse findSchedule(FindScheduleDTO findScheduleDTO) {
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
}




