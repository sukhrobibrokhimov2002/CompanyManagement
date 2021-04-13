package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.lesson5task1.entity.enums.MonthEnum;
import uz.pdp.lesson5task1.entity.template.AbsEntity;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class SalaryTaken extends AbsEntity {
    @ManyToOne
    private User owner;
    @Column(nullable = false)
    private double amount;
    @Enumerated(EnumType.STRING)
    private MonthEnum monthEnum;

    private  boolean paid=false;

}
