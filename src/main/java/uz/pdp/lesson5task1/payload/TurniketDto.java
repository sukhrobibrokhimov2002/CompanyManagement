package uz.pdp.lesson5task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurniketDto {

    private Integer companyId;
    private String ownerEmail;


}
