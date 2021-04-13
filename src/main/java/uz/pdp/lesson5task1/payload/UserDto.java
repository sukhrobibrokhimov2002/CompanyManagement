package uz.pdp.lesson5task1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserDto {
    @Size(min = 5,max = 50)
    @NotNull(message = "Full name  must not be empty")
    private String fullName;
    @Email
    @NotNull(message = "Email must not be empty")
    private String email;
    private String position;
    private Integer roleId;
}
