package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.QueryResponseDto;
import uz.pdp.lesson5task1.payload.UserDto;
import uz.pdp.lesson5task1.service.UserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody UserDto userDto, HttpServletRequest httpServletRequest) throws MessagingException {

        ApiResponse apiResponse = userService.add(httpServletRequest, userDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code) {

        ApiResponse apiResponse = userService.verifyEmail(email, code);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);

    }

    //direktorlarga manager va ishcilarni, managerlarga esa faqat xodimlarni ro'yhati ko'rinadi
    @GetMapping("/getUserAllInfo")
    public ResponseEntity<?> getAllInfo(HttpServletRequest httpServletRequest) {
        List<User> userInfo = userService.getUserAllInfo(httpServletRequest);
        if (userInfo.isEmpty()) return ResponseEntity.status(409).body(userInfo);
        return ResponseEntity.ok(userInfo);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = userService.deleteUser(httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @PatchMapping("edit")
    public ResponseEntity<?> editUserInfo(HttpServletRequest httpServletRequest, @Valid @RequestBody UserDto userDto) throws MessagingException {

        ApiResponse apiResponse = userService.editUser(httpServletRequest, userDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }


    @GetMapping("/getOneUserInfo")
    public ResponseEntity<?> getOnlyOneUserInfo(@RequestParam String email) {
        QueryResponseDto oneUserInfo = userService.getOneUserInfo(email);

        return ResponseEntity.ok(oneUserInfo);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
