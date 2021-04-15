package uz.pdp.lesson5task1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.lesson5task1.entity.Turnikit;
import uz.pdp.lesson5task1.payload.ApiResponse;
import uz.pdp.lesson5task1.payload.TurniketDto;
import uz.pdp.lesson5task1.service.TurniketService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/turniket")
public class TurniketController {

    @Autowired
    TurniketService turniketService;

    @PostMapping
    public ResponseEntity<?> giveTurnikit(HttpServletRequest httpServletRequest, @RequestBody TurniketDto turniketDto) {
        ApiResponse apiResponse = turniketService.addTurniket(httpServletRequest, turniketDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @GetMapping("/inOut")
    public ResponseEntity<?> enterAndExit(HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = turniketService.enterExit(httpServletRequest);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest httpServletRequest, @RequestParam String uniqueNumber) {
        ApiResponse delete = turniketService.delete(httpServletRequest, uniqueNumber);
        if (delete.isSuccess()) return ResponseEntity.ok(delete);
        return ResponseEntity.status(409).body(delete);
    }

    @GetMapping("/getInfo")
    public ResponseEntity<?> getInfo(HttpServletRequest httpServletRequest) {
        Turnikit turniketInfo = turniketService.getTurniketInfo(httpServletRequest);
        if (turniketInfo != null) return ResponseEntity.status(409).body(turniketInfo);
        return ResponseEntity.ok(turniketInfo);
    }


}
