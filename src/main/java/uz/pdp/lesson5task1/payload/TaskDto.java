package uz.pdp.lesson5task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.lesson5task1.entity.enums.TaskStatusEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    @NotNull(message = "The Name of the Task must not be null")
    private String taskName;
    @NotNull(message = "Description of the task must not be null")
    private String description;

    private Timestamp timestamp;
    private String taskTakerEmail;


}
