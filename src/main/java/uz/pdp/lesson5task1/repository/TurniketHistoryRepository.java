package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.TurnikitHistory;

import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurnikitHistory, UUID> {


    @Query(value = "select * from turnikit_history th where id=(select id from turnikit_history" +
            " where turnikit_id=:turnikitId order by timestamp desc limit 1)", nativeQuery = true)
    TurnikitHistory findByTurnikitOwnerAndTimestampAsc(UUID turnikitId);
}
