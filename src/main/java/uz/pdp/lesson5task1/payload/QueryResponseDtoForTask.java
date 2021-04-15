package uz.pdp.lesson5task1.payload;

import org.hibernate.annotations.Type;

import java.util.UUID;

public interface QueryResponseDtoForTask {

    String getFull_Name();

    String getEmail();

    String getTask_Name();


    String getTask_Giver_Id();

    String getTask_Status();

    String getDescription();
}
