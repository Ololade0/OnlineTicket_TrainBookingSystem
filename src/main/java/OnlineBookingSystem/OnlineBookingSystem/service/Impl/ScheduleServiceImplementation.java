package OnlineBookingSystem.OnlineBookingSystem.service.Impl;

import OnlineBookingSystem.OnlineBookingSystem.exceptions.InvalidScheduleException;
import OnlineBookingSystem.OnlineBookingSystem.exceptions.ScheduleDetailsException;
import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import OnlineBookingSystem.OnlineBookingSystem.model.Train;
import OnlineBookingSystem.OnlineBookingSystem.model.enums.ScheduleType;
import OnlineBookingSystem.OnlineBookingSystem.repositories.ScheduleRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.StationRepository;
import OnlineBookingSystem.OnlineBookingSystem.repositories.TrainRepository; // Add the TrainRepository import
import OnlineBookingSystem.OnlineBookingSystem.service.ScheduleService;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImplementation implements ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImplementation.class);

    private final ScheduleRepository scheduleRepository;
    private final StationRepository stationRepository;
    private final TrainRepository trainRepository; // Declare the TrainRepository

    @Transactional
    public List<Schedule> createSchedules(LocalDate date, Long trainId) throws InvalidScheduleException, ScheduleDetailsException {
        List<Schedule> scheduleList = new ArrayList<>();
        logger.info("Creating schedules for date: {}", date);


        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new ScheduleDetailsException("Train not found in the database."));

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
                stationRepository.findByStationName("AGEGE"),
                stationRepository.findByStationName("MONIYA"),
                stationRepository.findByStationName("AGBADO"),
                stationRepository.findByStationName("PAPALANTO"),
                stationRepository.findByStationName("ABEOKUTA"),
                stationRepository.findByStationName("OMI_ADIO"),
                stationRepository.findByStationName("EBUTTE_METTA_STATION")
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

        createMorningSchedules(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
        createAfternoonSchedules(date, scheduleList, train, agegeStation, moniyaStation, agbadoStation, papalantoStation, abeokutaStation, omiAdioStation, ebuteMettaStation);
    }

    private void createMorningSchedules(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                        Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) {
        scheduleList.add(createSchedule(moniyaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 0), train));
        scheduleList.add(createSchedule(omiAdioStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 20), train));
        scheduleList.add(createSchedule(abeokutaStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(8, 59), train));
        scheduleList.add(createSchedule(papalantoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 23), train));
        scheduleList.add(createSchedule(agbadoStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(9, 48), train));
        scheduleList.add(createSchedule(agegeStation, ebuteMettaStation, ScheduleType.MORNING, date, LocalTime.of(10, 3), train));
    }

    private void createAfternoonSchedules(LocalDate date, List<Schedule> scheduleList, Train train, Station agegeStation, Station moniyaStation,
                                          Station agbadoStation, Station papalantoStation, Station abeokutaStation, Station omiAdioStation, Station ebuteMettaStation) {
        scheduleList.add(createSchedule(ebuteMettaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 0), train));
        scheduleList.add(createSchedule(agegeStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 28), train));
        scheduleList.add(createSchedule(agbadoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(16, 43), train));
        scheduleList.add(createSchedule(papalantoStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 9), train));
        scheduleList.add(createSchedule(abeokutaStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(17, 35), train));
        scheduleList.add(createSchedule(omiAdioStation, moniyaStation, ScheduleType.EVENING, date, LocalTime.of(18, 11), train));
    }

    private Schedule createSchedule(Station departureStation, Station arrivalStation, ScheduleType scheduleType, LocalDate date, LocalTime departureTime, Train train) {
        LocalTime arrivalTime = departureTime.plusHours(1).plusMinutes(45);
        return Schedule.builder()
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .departureDate(date)
                .arrivalDate(date)
                .duration(Duration.between(departureTime, arrivalTime))
                .stations(List.of(departureStation, arrivalStation))
                .scheduleType(scheduleType)
                .train(train) // Associate train with the schedule
                .build();
    }
}