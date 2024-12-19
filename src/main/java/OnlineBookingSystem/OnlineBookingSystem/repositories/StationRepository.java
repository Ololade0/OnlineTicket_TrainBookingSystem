package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
//    @Query("SELECT s FROM Station s WHERE s.stationId = :id")
//    Optional<Station> findByStationId(@Param("id") Long id);
    Optional<Station> findByStationName(String stationName);


    boolean existsByStationName(String stationName);
    boolean existsByStationCode(String stationCode);
}
