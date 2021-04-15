package uz.pdp.lesson5task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryAmountDto {

    private String email;
    private double amount;
    private Integer monthsId;
}
