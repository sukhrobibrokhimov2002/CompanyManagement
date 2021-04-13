package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.lesson5task1.entity.enums.TurniketStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor

@NoArgsConstructor
@Entity
public class TurniketHistory {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @ManyToOne
    private Turnikit turnikit;
    @Enumerated(EnumType.STRING)
    private TurniketStatus turniketStatus;
    @CreationTimestamp
    private Timestamp timestamp;




}

