package uz.pdp.lesson5task1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.lesson5task1.entity.Company;
import uz.pdp.lesson5task1.entity.User;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.CompanyDto;
import uz.pdp.lesson5task1.repository.CompanyRepository;
import uz.pdp.lesson5task1.repository.UsersRepository;
import uz.pdp.lesson5task1.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse addCompany(CompanyDto companyDto) {
        Optional<User> byEmail = usersRepository.findByEmail(companyDto.getEmail());
        if (!byEmail.isPresent()) return new ApiResponse(false, "Director not found");
        Company company = new Company();
        company.setCompanyName(companyDto.getCompanyName());
        company.setDirectorName(byEmail.get());
        companyRepository.save(company);
        return new ApiResponse(true, "Company successfully added");

    }

    public List<Company> companyInfo() {
        List<Company> all = companyRepository.findAll();
        return all;
    }

    public Company getCompanyInfoByToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
        if (!byEmail.isPresent()) return null;
        User user = byEmail.get();
        Company byDirector = companyRepository.findByDirectorName(user);
        return byDirector;
    }

    public ApiResponse edit(HttpServletRequest httpServletRequest, CompanyDto companyDto) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
        if (!byEmail.isPresent()) return null;
        User user = byEmail.get();
        Company byDirector = companyRepository.findByDirectorName(user);

        //edit qilish
        Optional<User> optionalUser = usersRepository.findByEmail(companyDto.getEmail());
        if (!optionalUser.isPresent()) return new ApiResponse(false, "Director not found");
        byDirector.setCompanyName(companyDto.getCompanyName());
        byDirector.setDirectorName(optionalUser.get());
        companyRepository.save(byDirector);
        return new ApiResponse(true, "Company successfully edited");
    }


    public ApiResponse delete(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userNameFromToken = jwtProvider.getUserNameFromToken(token);
        Optional<User> byEmail = usersRepository.findByEmail(userNameFromToken);
        if (!byEmail.isPresent()) return null;
        User user = byEmail.get();
        Company byDirector = companyRepository.findByDirectorName(user);
        companyRepository.deleteById(byDirector.getId());
        return new ApiResponse(true, "Company successfully deleted");
    }
}
