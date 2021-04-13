package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.lesson5task1.entity.enums.RoleEnum;
import uz.pdp.lesson5task1.entity.enums.TaskStatusEnum;
import uz.pdp.lesson5task1.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)

public class Task extends AbsEntity {

    @Column(nullable = false)
    private String taskName;
    @Column(nullable = false)
    private String description;

    private Timestamp deadline;
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum taskStatus;

    @ManyToOne(optional = false)
    private User taskTaker; // a person who is doing task
    @ManyToOne(optional = false)
    private User taskGiver; // a person who give this task



}
