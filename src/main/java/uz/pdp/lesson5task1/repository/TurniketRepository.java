package uz.pdp.lesson5task1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.lesson5task1.entity.Turnikit;
import uz.pdp.lesson5task1.entity.User;

import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turnikit, UUID> {

    Turnikit findByOwner(User owner);

    void deleteByUniqueNumber(String uniqueNumber);
    boolean existsByOwner(User owner);
}
