package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.lesson5task1.entity.enums.MonthEnum;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Months {
    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private MonthEnum monthName;
}
