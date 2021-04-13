package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query(value = "select * from role where id=:roleId",nativeQuery = true)
    Optional<Role> findByRoleId(Integer roleId);


}
