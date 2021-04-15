package uz.pdp.lesson5task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.UUID;


public interface QueryResponseDto {
    String getFull_Name();
    String getEmail();
    String getPosition();
    String getTask_Name();

}
