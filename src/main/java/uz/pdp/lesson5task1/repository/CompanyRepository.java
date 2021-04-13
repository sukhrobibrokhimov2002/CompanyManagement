package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson5task1.entity.Company;
import uz.pdp.lesson5task1.entity.User;

public interface CompanyRepository extends JpaRepository<Company,Integer> {

    Company findByDirectorName(User directorName);
}
