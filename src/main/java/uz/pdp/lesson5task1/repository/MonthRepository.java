package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.lesson5task1.entity.Months;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.enums.MonthEnum;

import java.util.Optional;

public interface MonthRepository extends JpaRepository<Months,Integer> {


    @Query(value = "select * from months where month_name=:monthName",nativeQuery = true)
    Optional<Months> findMonthsByName(String monthName);



}
