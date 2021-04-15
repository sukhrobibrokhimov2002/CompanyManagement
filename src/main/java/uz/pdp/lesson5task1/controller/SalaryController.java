package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.entity.SalaryTaken;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.SalaryAmountDto;
import uz.pdp.lesson5task1.service.SalaryService;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;


    @PostMapping("/addSalary")
    public ResponseEntity<?> addSalary(@RequestBody SalaryAmountDto salaryAmountDto) {
        ApiResponse apiResponse = salaryService.addSalaryTable(salaryAmountDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/giveSalary")
    public ResponseEntity<?> giveSalary(@RequestParam String email, @RequestParam Integer monthsId) throws MessagingException {
        ApiResponse apiResponse = salaryService.giveSalary(email, monthsId);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/getSalariesByMonths")
    public ResponseEntity<?> getbyMonths(@RequestParam Integer monthsId) {
        List<SalaryTaken> byMonths = salaryService.getByMonths(monthsId);
        if (byMonths.isEmpty()) return ResponseEntity.status(409).body(byMonths);
        return ResponseEntity.ok(byMonths);
    }

    @GetMapping("/getByOwnerEmail")
    public ResponseEntity<?> getOwnerByEmail(@RequestParam String email) {
        List<SalaryTaken> byOwnerId = salaryService.getByOwnerId(email);
        if (byOwnerId.isEmpty()) return ResponseEntity.status(409).body(byOwnerId);
        return ResponseEntity.ok(byOwnerId);
    }
}
