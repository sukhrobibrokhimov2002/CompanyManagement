package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.entity.Company;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.CompanyDto;
import uz.pdp.lesson5task1.service.CompanyService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto companyDto) {
        ApiResponse apiResponse = companyService.addCompany(companyDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<Company> companies = companyService.companyInfo();
        if (companies.isEmpty()) return ResponseEntity.status(409).body(companies);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/getByToken")
    public ResponseEntity<?> getByToken(HttpServletRequest httpServletRequest) {
        Company companyInfoByToken = companyService.getCompanyInfoByToken(httpServletRequest);
        if (companyInfoByToken != null) return ResponseEntity.ok(companyInfoByToken);
        return ResponseEntity.status(409).body(companyInfoByToken);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(HttpServletRequest httpServletRequest) {
        ApiResponse delete = companyService.delete(httpServletRequest);
        if (!delete.isSuccess()) return ResponseEntity.status(409).body(delete);
        return ResponseEntity.ok(delete);
    }

    @PutMapping
    public ResponseEntity<?> edit(HttpServletRequest httpServletRequest, @RequestBody CompanyDto companyDto) {
        ApiResponse apiResponse = companyService.edit(httpServletRequest, companyDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }
}
