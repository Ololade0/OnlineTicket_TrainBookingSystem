package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM schedules s " +
            "JOIN s.stations departureStation " +
            "JOIN s.stations arrivalStation " +
            "WHERE departureStation.stationName = :departureStationName " +
            "AND arrivalStation.stationName = :arrivalStationName " +
            "AND s.departureDate = :date")
    List<Schedule> findSchedule(@Param("arrivalStationName") String arrivalStationName,
                                @Param("departureStationName") String departureStationName,
                                @Param("date") LocalDate date);
}
