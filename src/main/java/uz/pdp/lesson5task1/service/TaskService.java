package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.Component.CheckingAuthorities;
import uz.pdp.lesson5task1.Component.MailSender;
import uz.pdp.lesson5task1.entity.Task;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.entity.enums.TaskStatusEnum;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.TaskDto;
import uz.pdp.lesson5task1.repository.TaskRepository;
import uz.pdp.lesson5task1.repository.UsersRepository;
import uz.pdp.lesson5task1.security.JwtProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    MailSender mailSender;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CheckingAuthorities checkingAuthorities;
    @Autowired
    TaskRepository taskRepository;


    public ApiResponse giveTask(HttpServletRequest httpServletRequest, TaskDto taskDto) throws MessagingException {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> optionalUser = usersRepository.findByEmail(userNameFromToken);
        if (!optionalUser.isPresent()) return new ApiResponse(false, "Task giver not found");
        User taskGiver = optionalUser.get();

        Optional<User> taskTakerOptional = usersRepository.findByEmail(taskDto.getTaskTakerEmail());
        if (!taskTakerOptional.isPresent()) return new ApiResponse(false, "Task taker not found");
        User taskTaker = taskTakerOptional.get();

        boolean checkAuthorityForTask = checkingAuthorities.checkAuthorityForTask(httpServletRequest, taskTaker);
        if (!checkAuthorityForTask) return new ApiResponse(false, "You have not got right for giving task this person");
        Task task = new Task();
        task.setTaskGiver(taskGiver);
        task.setTaskTaker(taskTaker);
        task.setTaskName(taskDto.getTaskName());
        task.setTaskStatus(TaskStatusEnum.TASK_NOT_STARTED);
        task.setDeadline(taskDto.getTimestamp());
        task.setDescription(taskDto.getDescription());
        Task save = taskRepository.save(task);

        boolean mailTextForGivingTask = mailSender.mailTextForGivingTask(taskTaker.getEmail(), save.getTaskName(), taskGiver.getFullName(), save.getDescription(), save.getDeadline());
        if (mailTextForGivingTask) return new ApiResponse(true, "Task Emailga jo'natildi");
        return new ApiResponse(false, "Emailga yuborishda xatolik");

    }

    public ApiResponse verifyTask(String email, String taskName) {
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (!optionalUser.isPresent()) return new ApiResponse(false, "Xatolik");


        Task byTaskName = taskRepository.findByTaskNameAndTaskTaker(taskName, optionalUser.get());
        if (byTaskName == null) return new ApiResponse(false, "Task topilmadi");
        if (byTaskName.getTaskTaker().equals(optionalUser.get())) {
            byTaskName.setTaskStatus(TaskStatusEnum.TASK_IN_PROGRESS);
            taskRepository.save(byTaskName);
            return new ApiResponse(true, "Taskga start berildi");

        }
        return new ApiResponse(false, "Xatolik");
    }

    public ApiResponse taskCompleted(HttpServletRequest httpServletRequest, String taskName) throws MessagingException {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
        if (!byEmail.isPresent()) return new ApiResponse(false, "NOt found");
        Task byTaskNameAndTaskTaker = taskRepository.findByTaskNameAndTaskTaker(taskName, byEmail.get());
        if (byTaskNameAndTaskTaker == null) return new ApiResponse(false, "Task not found");
        byTaskNameAndTaskTaker.setTaskStatus(TaskStatusEnum.TASK_COMPLETED);
        taskRepository.save(byTaskNameAndTaskTaker);
        boolean mailForComplete = mailSender.mailForComplete(byTaskNameAndTaskTaker.getTaskGiver().getEmail(), taskName, byTaskNameAndTaskTaker.getDescription(), byEmail.get().getFullName());
        if (mailForComplete) return new ApiResponse(true, "Successfully completed");
        return new ApiResponse(true, "Emailda xatolik");

    }

    public List<Task> getUserTasks(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
        if (!byEmail.isPresent()) return null;
        List<Task> byTaskTaker = taskRepository.findByTaskTaker(byEmail.get());
        return byTaskTaker;

    }

}
