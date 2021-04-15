package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.Component.CheckingAuthorities;
import uz.pdp.lesson5task1.Component.MailSender;
import uz.pdp.lesson5task1.Component.PasswordGenerator;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.entity.enums.RoleEnum;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.QueryResponseDto;
import uz.pdp.lesson5task1.payload.UserDto;
import uz.pdp.lesson5task1.repository.RoleRepository;
import uz.pdp.lesson5task1.repository.UsersRepository;
import uz.pdp.lesson5task1.security.JwtProvider;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    CheckingAuthorities checkingAuthorities;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordGenerator passwordGenerator;
    @Autowired
    MailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ApiResponse add(UserDto userDto) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> userOptional = usersRepository.findById(user.getId());

        boolean existsByEmail = usersRepository.existsByEmail(userDto.getEmail());
        if (existsByEmail)
            return new ApiResponse(false, "Bunday emailli user mavjud");

        Optional<Role> optionalRole = roleRepository.findByRoleId(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse(false, "role not found");

        boolean checkAuthority = checkingAuthorities.checkAuthority( optionalRole.get().getName().name());
        if (!checkAuthority) return new ApiResponse(false, "You have not got right for doing this");

        User users = new User();
        users.setEmail(userDto.getEmail());
        String randomPassword = passwordGenerator.generateRandomPassword(3);
        users.setPassword(passwordEncoder.encode(randomPassword));
        users.setFullName(userDto.getFullName());

        //verify password generatsiya qilindi
        String code = String.valueOf(UUID.randomUUID());

        Set<Role> roleSet = new HashSet<>();
        roleSet.add(optionalRole.get());
        users.setRoles(roleSet);
        users.setVerifyPassword(code);
        users.setPosition(userDto.getPosition());
        usersRepository.save(users);

        //emailga xabar jo'natish
        boolean mailTextAdd = mailSender.mailTextAdd(userDto.getEmail(), code, randomPassword);

        if (mailTextAdd)
            return new ApiResponse(true, "Emailga xabar yuborildi");
        return new ApiResponse(false, "Xatolik");
    }


    public ApiResponse verifyEmail(String email, String code) {
        try {
            Optional<User> userOptional = usersRepository.findByEmail(email);
            if (!userOptional.isPresent()) return new ApiResponse(false, "Xatolik");

            User user = userOptional.get();
            if (user.getEmail().equals(email) && user.getVerifyPassword().equals(code)) {
                user.setEnabled(true);
                user.setVerifyPassword(null);
                usersRepository.save(user);

                return new ApiResponse(true, "Email tasdiqlandi");
            }

        } catch (Exception e) {
            return new ApiResponse(false, "Xatolik");
        }
        return new ApiResponse(false, "Xatolik");

    }

    public List<User> getUserAllInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = usersRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return null;

        Set<Role> roles = optionalUser.get().getRoles();
//        if (roles.size() >= 1 && roles.contains(RoleEnum.ROLE_DIRECTOR.name())) {
//            List<User> forDirector = usersRepository.findForDirector();
//            return forDirector;
//        } else if (roles.size() >= 1 && roles.contains(RoleEnum.ROLE_MANAGER.name())) {
//            List<User> forManager = usersRepository.findForManager();
//            return forManager;
//        } else if (roles.size() == 1 && roles.contains(RoleEnum.ROLE_WORKER.name())) {
//            Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
//            if (!byEmail.isPresent()) return null;
//            return Collections.singletonList(byEmail.get());
//        }
//        return null;

        int userRoles = 0;

        for (Role role : roles) {

            if (role.getName().name().equals(RoleEnum.ROLE_DIRECTOR.name())) {
                userRoles = 1;
            }
            if (role.getName().name().equals(RoleEnum.ROLE_MANAGER.name())) {
                userRoles = 2;
            }
            if (role.getName().name().equals(RoleEnum.ROLE_WORKER.name())) {
                userRoles = 3;
            }
        }

        if (userRoles == 1) {
            List<User> forDirector = usersRepository.findForDirector();
            return forDirector;
        } else if (userRoles == 2) {
            List<User> forManager = usersRepository.findForManager();
            return forManager;
        } else if (userRoles == 3) {
            Optional<User> byEmail = usersRepository.findById(user.getId());
            if (!byEmail.isPresent()) return null;
            return Collections.singletonList(byEmail.get());
        }
        return null;
    }

    public ApiResponse deleteUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> userOptional = usersRepository.findById(user.getId());
        if (!userOptional.isPresent()) return new ApiResponse(false, "User not found");
        usersRepository.deleteById(userOptional.get().getId());
        return new ApiResponse(true, "Successfully deleted");
    }

    public ApiResponse editUser( UserDto userDto) throws MessagingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> optionalUser = usersRepository.findById(user.getId());
        if (!optionalUser.isPresent()) return new ApiResponse(false, "User not found");
        Optional<Role> optionalRole = roleRepository.findByRoleId(userDto.getRoleId());
        if (!optionalRole.isPresent()) return new ApiResponse(false, "Role not found");

        User user1 = optionalUser.get();
        boolean existsByEmailAndIdNot = usersRepository.existsByEmailAndIdNot(userDto.getEmail(), user.getId());
        if (existsByEmailAndIdNot) return new ApiResponse(false, "Bunday email mavjud");

        user.setFullName(userDto.getFullName());
        user.setPosition(userDto.getPosition());
        user.setRoles(user.getRoles());
        user.setEmail(userDto.getEmail());
        String generateRandomPassword = passwordGenerator.generateRandomPassword(8);
        user.setPassword(passwordEncoder.encode(generateRandomPassword));

        //verify password generatsiya qilindi
        String code = String.valueOf(UUID.randomUUID());
        user.setVerifyPassword(code);

        usersRepository.save(user);
        boolean mailTextAdd = mailSender.mailTextAdd(userDto.getEmail(), code, generateRandomPassword);
        if (mailTextAdd)
            return new ApiResponse(true, "Emailni tasdiqlang");

        return new ApiResponse(false, "Xatolik");

    }

    public QueryResponseDto getOneUserInfo(String email) {
        boolean existsByEmail = usersRepository.existsByEmail(email);
        if (!existsByEmail) return null;
        QueryResponseDto userWithTasks = usersRepository.findUserWithTasks(email);
        return userWithTasks;

    }

}
