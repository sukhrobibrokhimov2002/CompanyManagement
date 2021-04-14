package uz.pdp.lesson5task1.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import uz.pdp.lesson5task1.entity.Company;
import uz.pdp.lesson5task1.entity.Role;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.entity.enums.RoleEnum;
import uz.pdp.lesson5task1.repository.CompanyRepository;
import uz.pdp.lesson5task1.repository.RoleRepository;
import uz.pdp.lesson5task1.repository.UsersRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UsersRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            Role role=new Role();
            role.setName(RoleEnum.ROLE_ADMIN);
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            User user = new User("Sukhrob Ibrokhimov",passwordEncoder.encode("123"),"sukhrobjon2002@gmail.com","Admin of the system",roles,true);
            User director = userRepository.save(user);
            Company company = new Company("PDP", director);
            Company save = companyRepository.save(company);
        }
    }
}
