package uz.pdp.lesson5task1.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.entity.enums.RoleEnum;
import uz.pdp.lesson5task1.repository.UsersRepository;
import uz.pdp.lesson5task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Component
public class CheckingAuthorities {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UsersRepository usersRepository;

    public boolean checkAuthority(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = usersRepository.findById(user.getId());

        if (!userOptional.isPresent()) return false;

        Set<Role> adminRoles = userOptional.get().getRoles(); //user qo'shayotgan odamning roli
        String position = userOptional.get().getPosition();

        //Direktor rolidagi userni faqat admin qo'sha oladi
//        if (role.equals(RoleEnum.ROLE_DIRECTOR.name()))
//            return false;

        for (Role adminRole : adminRoles) {
            String roleName = adminRole.getName().name();
            //admin direktorni,managerni,ishchini qo'sha oladi
            if (roleName.equals(RoleEnum.ROLE_ADMIN.name()) &&
                    (role.equals(RoleEnum.ROLE_DIRECTOR.name())
                            || role.equals(RoleEnum.ROLE_MANAGER.name())
                            || role.equals(RoleEnum.ROLE_WORKER.name())))
                return true;

            //managerni faqat admin qo'sha oladi
            if (roleName.equals(RoleEnum.ROLE_DIRECTOR.name()) &&
                    role.equals(RoleEnum.ROLE_MANAGER.name())) return true;

            //ishchini HRManager va direktor qo'sha oladi
            if (role.equals(RoleEnum.ROLE_WORKER.name()) &&
                    (roleName.equals(RoleEnum.ROLE_DIRECTOR.name()) ||
                            (roleName.equals(RoleEnum.ROLE_MANAGER.name())
                                    && position.equalsIgnoreCase("hrmanager"))))
                return true;

        }
        return false;


    }

    public boolean checkAuthorityForTask(User taskTaker) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> taskGiver = usersRepository.findById(user.getId());
        if (!taskGiver.isPresent()) return false;
        int taskGiverRole = 0;  //Task beruvchi rolini aniqlab olish uchun

        for (Role role : taskGiver.get().getRoles()) {
            String roleName = role.getName().name();
            if (roleName.equals(RoleEnum.ROLE_DIRECTOR.name())) {
                taskGiverRole = 1;
            }
            if (roleName.equals(RoleEnum.ROLE_MANAGER.name())) {
                taskGiverRole = 2;
            }
            if (roleName.equals(RoleEnum.ROLE_WORKER.name())) {
                taskGiverRole = 3;
            }
        }

        for (Role takerRole : taskTaker.getRoles()) {
            String takerRoleName = takerRole.getName().name();
            if (taskGiverRole == 1 && (takerRoleName.equals(RoleEnum.ROLE_MANAGER.name()))
                    || takerRoleName.equals(RoleEnum.ROLE_WORKER.name()))
                return true;

            if (taskGiverRole == 2 && takerRoleName.equals(RoleEnum.ROLE_WORKER.name()))
                return true;
            if (taskGiverRole == 3) return false;
        }
        return false;


    }

    public boolean checkAuthorityforTurniket(User turniketOwner) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Optional<User> byEmail = usersRepository.findById(user.getId());
        Set<Role> giverRoles = byEmail.get().getRoles();

        int giverRolesStatus = 0;
        for (Role giverRole : giverRoles) {
            String giverRoleName = giverRole.getName().name();
            if (giverRoleName.equals(RoleEnum.ROLE_DIRECTOR.name())) {
                giverRolesStatus = 1;
            }
            if (giverRoleName.equals(RoleEnum.ROLE_MANAGER.name())) {
                giverRolesStatus = 2;
            }
            if (giverRoleName.equals(RoleEnum.ROLE_WORKER.name())) {
                giverRolesStatus = 3;
            }
        }


        Set<Role> ownerRoles = turniketOwner.getRoles();

        for (Role roleName : ownerRoles) {
            String ownerRoleName = roleName.getName().name();
            if (ownerRoleName.equals(RoleEnum.ROLE_WORKER.name())
                    && (giverRolesStatus == 1 || giverRolesStatus == 2)) return true;
        }
        return false;


    }

}
