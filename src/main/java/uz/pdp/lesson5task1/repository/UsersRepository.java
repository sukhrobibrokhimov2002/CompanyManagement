package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.payload.QueryResponseDto;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "select * from users " +
            "where id in (select users_id from " +
            "users_roles where roles_id=2 or roles_id=3)", nativeQuery = true)
    List<User> findForDirector();

    @Query(value = "select * from users \" +\n" +
            "            \"where id in (select users_id from \" +\n" +
            "            \"users_roles where  roles_id=3)", nativeQuery = true)
    List<User> findForManager();


    boolean existsByEmailAndIdNot(String email, UUID id);

    @Query(value = "select us.full_name,us.email,us.position,ts.task_name from users us join task ts on ts.task_taker_id=us.id " +
            "where ts.task_status='TASK_COMPLETED' and us.email=:email", nativeQuery = true)
    QueryResponseDto findUserWithTasks(String email);


}
