package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.TaskDto;
import uz.pdp.lesson5task1.service.TaskService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;


    @PostMapping
    public ResponseEntity<?> giveTask(HttpServletRequest httpServletRequest, @RequestBody TaskDto taskDto) throws MessagingException {
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
}
