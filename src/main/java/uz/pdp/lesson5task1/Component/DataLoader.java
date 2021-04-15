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
import java.util.Optional;
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
            Optional<Role> byRoleId = roleRepository.findByRoleId(4);
            Set<Role> roles = new HashSet<>();
            roles.add(byRoleId.get());
            User user = new User();
            user.setEmail("sukhrobjon0901@gmail.com");
            user.setFullName("Sukhrob Ibrokhimov");
            user.setPassword(passwordEncoder.encode("123"));
            user.setRoles(roles);
            user.setPosition("Admin of the system");
            user.setEnabled(true);

            User director = userRepository.save(user);
            Company company = new Company("PDP", director);
            Company save = companyRepository.save(company);
        }
    }
}
