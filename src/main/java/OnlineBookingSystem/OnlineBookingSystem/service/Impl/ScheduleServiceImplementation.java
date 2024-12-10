package OnlineBookingSystem.OnlineBookingSystem.service.Impl;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.ScheduleResponse;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.FindScheduleDTO;
import OnlineBookingSystem.OnlineBookingSystem.dto.response.request.UpdateScheduleDto;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleCannotBeFoundException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Fare;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.Route;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.ScheduleRepository;
import OnlineBookingSystem.OnlineBookingSystem.service.ScheduleService;
import OnlineBookingSystem.OnlineBookingSystem.service.StationService;
import OnlineBookingSystem.OnlineBookingSystem.service.TrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImplementation implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImplementation.class);

    private final ScheduleRepository scheduleRepository;
    private final StationService stationService;
    private final TrainService trainService;

    @Transactional
    public List<Schedule> createSchedules(LocalDate date, Long trainId) throws InvalidScheduleException, ScheduleDetailsException {
        List<Schedule> scheduleList = new ArrayList<>();
        logger.info("Creating schedules for date: {}", date);

        Train train = trainService.findTrainById(trainId);
//        validateScheduleInputs(date,train,scheduleList);
        TrainTimetable(date, scheduleList, train);

        if (scheduleList.isEmpty()) {
            logger.warn("No schedules created for date: {}", date);
            return scheduleList;
        }

        logger.info("Number of schedules to save: {}", scheduleList.size());
        try {
            return scheduleRepository.saveAll(scheduleList);
        } catch (Exception e) {
            logger.error("Error saving schedules: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save schedules", e);
        }
    }


    private void TrainTimetable(LocalDate date, List<Schedule> scheduleList, Train train) throws InvalidScheduleException, ScheduleDetailsException {
        List<Optional<Station>> stations = List.of(
                stationService.findStationByName("AGEGE"),
                stationService.findStationByName("MONIYA"),
                stationService.findStationByName("AGBADO"),
                stationService.findStationByName("PAPALANTO"),
                stationService.findStationByName("ABEOKUTA"),
                stationService.findStationByName("OMI_ADIO"),
                stationService.findStationByName("EBUTTE_METTA_STATION")
        );

        if (stations.stream().anyMatch(Optional::isEmpty)) {
            logger.error("One or more stations are missing in the database.");
            throw new ScheduleDetailsException("Required stations are not available in the database.");
        }

        Station agegeStation = stations.get(0).get();
        Station moniyaStation = stations.get(1).get();
        Station agbadoStation = stations.get(2).get();
        Station papalantoStation = stations.get(3).get();
        Station abeokutaStation = stations.get(4).get();
        Station omiAdioStation = stations.get(5).get();
        Station ebuteMettaStation = stations.get(6).get();

        createMorningSchedulesFromIbadanToLagos(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation, Route.IBADAN_LAGOS_MORNING_TRAIN);
        createAfternoonSchedulesFromIbadanToLagos(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
        createMorningSchedulesFromLagosToIbadan(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
        createAfternoonSchedulesFromLagosToIbadan(date, scheduleList, train, agegeStation, moniyaStation,
                agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
    }


    //    MORNING SCHEDULE FROM  IBADAN TO LAGOS
    private void createMorningSchedulesFromIbadanToLagos(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                                         Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation, Route route) throws InvalidScheduleException {
        scheduleList.add(createSchedule(moniyaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 0), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
        scheduleList.add(createSchedule(omiAdioStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 20), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
        scheduleList.add(createSchedule(abeokutaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 59), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
        scheduleList.add(createSchedule(papalantoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 23), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
        scheduleList.add(createSchedule(agbadoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 48), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
        scheduleList.add(createSchedule(agegeStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(10, 3), train, Route.IBADAN_LAGOS_MORNING_TRAIN));
    }

    // AFTERNOON SCHEDULE FROM IBADAN TO LAGOS
    private void createAfternoonSchedulesFromIbadanToLagos(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                                           Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) throws InvalidScheduleException {
        scheduleList.add(createSchedule(moniyaStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(16, 0), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(omiAdioStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(16, 20), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(abeokutaStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(16, 59), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(papalantoStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(17, 23), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(agbadoStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(17, 48), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(agegeStation, ebuteMettaStation, ScheduleType.AFTERNOON, date, LocalTime.of(18, 3), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
    }

    //    MORNING SCHEDULE  FROM  LAGOS TO IBADAN
    private void createMorningSchedulesFromLagosToIbadan(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                                         Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) throws InvalidScheduleException {
        scheduleList.add(createSchedule(ebuteMettaStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(8, 0), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
        scheduleList.add(createSchedule(agegeStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(8, 28), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
        scheduleList.add(createSchedule(agbadoStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(8, 43), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
        scheduleList.add(createSchedule(papalantoStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(9, 9), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
        scheduleList.add(createSchedule(abeokutaStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(9, 35), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
        scheduleList.add(createSchedule(omiAdioStation, moniyaStation, ScheduleType.AFTERNOON, date, LocalTime.of(10, 11), train, Route.LAGOS_IBADAN_MORNING_TRAIN));
    }


    //    AFTERNOON SCHEDULE  FROM  LAGOS TO IBADAN
    private void createAfternoonSchedulesFromLagosToIbadan(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                                           Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) throws InvalidScheduleException {
        scheduleList.add(createSchedule(ebuteMettaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 0), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(agegeStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 28), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(agbadoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 43), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(papalantoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 9), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(abeokutaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 35), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
        scheduleList.add(createSchedule(omiAdioStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(18, 11), train, Route.IBADAN_LAGOS_AFTERNOON_TRAIN));
    }

    private Schedule createSchedule(Station departureStation, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, Train train, Route route) throws InvalidScheduleException {
        LocalTime arrivalTime = departureTime.plusHours(1).plusMinutes(45);
        validateScheduleTimetable(departureStation, arrivalStation, scheduleType, date, departureTime, arrivalTime);
        return Schedule.builder()
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .departureDate(date)
                .arrivalDate(date)
                .route(route)
                .duration(Duration.between(departureTime, arrivalTime))
                .stations(List.of(departureStation, arrivalStation))
                .scheduleType(scheduleType)
                .train(train)
                .build();
    }


    private static void validateScheduleTimetable(Station departureStations, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, LocalTime arrivalTime) throws ScheduleDetailsException, InvalidScheduleException {
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

//        LocalDate maxScheduleDate = currentDate.plusDays(2);
//        if (date.isAfter(maxScheduleDate)) {
//            throw new InvalidScheduleException("Schedules can only be created for up to 2 days in advance.");
//
//        }

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
// Ensure this import is present

//    public List<TimetableResponse> timetableForMovt() {
//        List<Schedule> schedules = scheduleRepository.findAll(); // Adjust this method to fetch all schedules
//        List<TimetableResponse> timetableResponses = new ArrayList<>();
//
//        for (int i = 0; i < schedules.size(); i++) {
//            Schedule schedule = schedules.get(i);
//            TimetableResponse response = new TimetableResponse(
//                    i + 1,
//                    schedule.getStations().toString(), // Assuming you have getCode() method in Station
////                    schedule.getDepartureStation().getName(),
//                    schedule.getArrivalTime(),
//                    schedule.getDepartureTime()
////                    calculateDistance(schedule.getDepartureStation(), schedule.getArrivalStation()) // Implement this method
//            );
//            timetableResponses.add(response);
//        }
//
//        return timetableResponses;
//    }
//
//    // Method to calculate distance (You need to implement this based on your logic)
//    private double calculateDistance(Station departureStation, Station arrivalStation) {
//        // Logic to calculate distance between departure and arrival stations
//        return 0; // Replace with actual distance
//    }

}




