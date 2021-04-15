package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Months;
import uz.pdp.lesson5task1.entity.SalaryTaken;
import uz.pdp.lesson5task1.entity.Task;
import uz.pdp.lesson5task1.entity.User;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<SalaryTaken, UUID> {

    SalaryTaken findByOwnerAndMonths(User owner, Months months);

    @Query(value = "select * from salary_taken where owner_id=:ownerId and paid=true",nativeQuery = true)
    List<SalaryTaken> findByOwners(UUID ownerId);

    @Query(value = "select * from salary_taken where months_id=:monthsId and paid=true",nativeQuery = true)
    List<SalaryTaken> findByMonths(Integer monthsId);


}
