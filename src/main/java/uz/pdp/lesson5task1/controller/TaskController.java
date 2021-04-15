package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.entity.Task;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.QueryResponseDtoForTask;
import uz.pdp.lesson5task1.payload.TaskDto;
import uz.pdp.lesson5task1.service.TaskService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;


    @PostMapping
    public ResponseEntity<?> giveTask(HttpServletRequest httpServletRequest, @Valid @RequestBody TaskDto taskDto) throws MessagingException {
        ApiResponse apiResponse = taskService.giveTask(httpServletRequest, taskDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }


    @GetMapping("/verifyTask")
    public ResponseEntity<?> verifyTask(@RequestParam String email, @RequestParam String taskName) {
        ApiResponse apiResponse = taskService.verifyTask(email, taskName);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/completeTask")
    public ResponseEntity<?> completeTask(HttpServletRequest httpServletRequest, @RequestParam String taskName) throws MessagingException {
        ApiResponse apiResponse = taskService.taskCompleted(httpServletRequest, taskName);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/getUserTaskInfo")
    public ResponseEntity<?> getUserTaskInfo(HttpServletRequest httpServletRequest) {
        List<Task> userTasks = taskService.getUserTasks(httpServletRequest);
        if (userTasks.isEmpty()) return ResponseEntity.status(409).body(userTasks);
        return ResponseEntity.ok(userTasks);

    }

    @GetMapping("/getCompletedTasks")
    public ResponseEntity<?> getCompletedTasks() {
        List<QueryResponseDtoForTask> completedTask = taskService.getCompletedTask();
        if (completedTask.isEmpty()) return ResponseEntity.status(409).body(completedTask);
        return ResponseEntity.ok(completedTask);
    }

    @GetMapping("/getOutDatedTasks")
    public ResponseEntity<?> getOutDatedTasks() {
        List<QueryResponseDtoForTask> outdatedTask = taskService.getOutdatedTask();
        if (outdatedTask.isEmpty()) return ResponseEntity.status(409).body(outdatedTask);
        return ResponseEntity.ok(outdatedTask);
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
