package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.Component.CheckingAuthorities;
import uz.pdp.lesson5task1.entity.*;
import uz.pdp.lesson5task1.entity.enums.RoleEnum;
import uz.pdp.lesson5task1.entity.enums.TurniketStatus;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.TurniketDto;
import uz.pdp.lesson5task1.repository.*;
import uz.pdp.lesson5task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Service
public class TurniketService {

    @Autowired
    UsersRepository usersRepository;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CheckingAuthorities checkingAuthorities;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;

    public ApiResponse addTurniket(HttpServletRequest httpServletRequest, TurniketDto turniketDto) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> userOptional = usersRepository.findByEmail(userNameFromToken);
        if (!userOptional.isPresent()) return new ApiResponse(false, "Turniket giver not found");
        User turniketGiver = userOptional.get();


        //turniket berilayotgan odam
        Optional<User> ownerUserOptional = usersRepository.findByEmail(turniketDto.getOwnerEmail());
        if (!ownerUserOptional.isPresent()) return new ApiResponse(false, "Siz kiritgan emailda user mavjud emas");
        User turniketOwner = ownerUserOptional.get();

        boolean existsByOwner = turniketRepository.existsByOwner(turniketOwner);
        if (existsByOwner) return new ApiResponse(false, "BU xodimda turnikit mavjud");


        boolean checkAuthorityforTurniket = checkingAuthorities.checkAuthorityforTurniket(httpServletRequest, turniketOwner);
        if (!checkAuthorityforTurniket) return new ApiResponse(false, "You have not such a right");

        Optional<Company> optionalCompany = companyRepository.findById(turniketDto.getCompanyId());
        if (!optionalCompany.isPresent()) return new ApiResponse(false, "Company not found");


        Turnikit turnikit = new Turnikit();
        turnikit.setCompany(optionalCompany.get());
        turnikit.setOwner(turniketOwner);
        turniketRepository.save(turnikit);
        return new ApiResponse(true, "Successfully added");


    }

    public ApiResponse enterExit(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);

        Optional<User> optionalUser = usersRepository.findByEmail(userNameFromToken);
        if (!optionalUser.isPresent()) return new ApiResponse(false, "User not found");
        User owner = optionalUser.get();

        Turnikit byOwnerTurniket = turniketRepository.findByOwner(owner);
        TurnikitHistory byTurnikit_ownerAndOrderByTimestampAsc = turniketHistoryRepository.findByTurnikitOwnerAndTimestampAsc(byOwnerTurniket.getId());

        TurnikitHistory turniketHistory = new TurnikitHistory();
        if (byTurnikit_ownerAndOrderByTimestampAsc == null) {

            turniketHistory.setTurniketStatus(TurniketStatus.WORKER_IN);
            turniketHistory.setTurnikit(byOwnerTurniket);
            turniketHistoryRepository.save(turniketHistory);
            return new ApiResponse(true, "Ishchi binoga kirdi");
        } else if (byTurnikit_ownerAndOrderByTimestampAsc != null) {
            if (byTurnikit_ownerAndOrderByTimestampAsc.getTurniketStatus().name().equals(TurniketStatus.WORKER_IN.name())) {
                turniketHistory.setTurnikit(byOwnerTurniket);
                turniketHistory.setTurniketStatus(TurniketStatus.WORKER_OUT);
                turniketHistoryRepository.save(turniketHistory);
                return new ApiResponse(true, "Ishchi binodan chiqdi");
            } else if (byTurnikit_ownerAndOrderByTimestampAsc.getTurniketStatus().name().equals(TurniketStatus.WORKER_OUT.name())) {
                turniketHistory.setTurnikit(byOwnerTurniket);
                turniketHistory.setTurniketStatus(TurniketStatus.WORKER_IN);
                turniketHistoryRepository.save(turniketHistory);
                return new ApiResponse(true, "Ishchi binoga kirdi");
            }
        }
        return new ApiResponse(false, "Xatolik");


    }

    public ApiResponse delete(HttpServletRequest httpServletRequest, String uniqueNumber) {

        try {
            String token = httpServletRequest.getHeader("Authorization");
            token = token.substring(7);
            String userNameFromToken = jwtProvider.getUserNameFromToken(token);
            Optional<User> optionalUser = usersRepository.findByEmail(userNameFromToken);
            User deleter = optionalUser.get();
            Set<Role> roles = deleter.getRoles();
            int deleterRoleStatus = 0;
            for (Role giverRole : roles) {
                String giverRoleName = giverRole.getName().name();
                if (giverRoleName.equals(RoleEnum.ROLE_DIRECTOR.name())) {
                    deleterRoleStatus = 1;
                }
                if (giverRoleName.equals(RoleEnum.ROLE_MANAGER.name())) {
                    deleterRoleStatus = 2;
                }
                if (giverRoleName.equals(RoleEnum.ROLE_WORKER.name())) {
                    deleterRoleStatus = 3;
                }
            }

            if (deleterRoleStatus == 1) {
                turniketRepository.deleteByUniqueNumber(uniqueNumber);
                return new ApiResponse(true, "Successfully deleted");
            }
            return new ApiResponse(false, "Sizda o'chirish uchun huqu mavjud emas");

        } catch (Exception e) {
            return new ApiResponse(false, "Xatolik");
        }
    }

    public Turnikit getTurniketInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> optionalUser = usersRepository.findByEmail(userNameFromToken);
        Turnikit byOwner = turniketRepository.findByOwner(optionalUser.get());
        return byOwner;


    }


}
