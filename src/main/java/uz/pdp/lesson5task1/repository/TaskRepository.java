package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.Task;
import uz.pdp.lesson5task1.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Task findByTaskNameAndTaskTaker(String taskName, User taskTaker);
    List<Task> findByTaskTaker(User taskTaker);



}
