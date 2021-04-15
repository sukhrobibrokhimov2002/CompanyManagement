package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.Task;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.payload.QueryResponseDtoForTask;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Task findByTaskNameAndTaskTaker(String taskName, User taskTaker);

    List<Task> findByTaskTaker(User taskTaker);

    @Query(value = "select us.full_name,us.email,ts.task_name,CAST (ts.task_giver_id as varchar ),ts.description,ts.task_status " +
            "from users us join task ts on us.id=ts.task_taker_id where task_status='TASK_COMPLETED'", nativeQuery = true)
    List<QueryResponseDtoForTask> getByTaskCompleted();

    @Query(value = "select us.full_name,us.email,ts.task_name,CAST (ts.task_giver_id as varchar ),ts.task_status,ts.description from users us join task ts on us.id=ts.task_taker_id where task_status='TASK_IN_PROGRESS' \n" +
            "and ts.deadline<NOW()", nativeQuery = true)
    List<QueryResponseDtoForTask> getByOutDated();


}
