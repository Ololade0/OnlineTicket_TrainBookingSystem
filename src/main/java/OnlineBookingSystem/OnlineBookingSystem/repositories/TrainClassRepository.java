package OnlineBookingSystem.OnlineBookingSystem.repositories;

import OnlineBookingSystem.OnlineBookingSystem.model.TrainClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainClassRepository extends JpaRepository<TrainClass, Long> {
        Optional<TrainClass> findByClassName(String trainClassName);

//        @Query("SELECT t FROM TrainClass t WHERE LOWER(t.name) = LOWER(:name)")
//        Optional<TrainClass> findByClassNameIgnoreCase(@Param("name") String name);




}
