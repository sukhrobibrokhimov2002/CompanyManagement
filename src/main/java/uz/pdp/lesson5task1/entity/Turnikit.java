package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.lesson5task1.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Turnikit extends AbsEntity {
    @ManyToOne
    private Company company;
    @OneToOne
    private User owner;
    private String uniqueNumber= UUID.randomUUID().toString();

}
