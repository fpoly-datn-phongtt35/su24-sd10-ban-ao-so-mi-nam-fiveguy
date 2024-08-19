package com.example.demo.restController.point;

import com.example.demo.service.point.PointSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class PointSettingsHomeController {

    @Autowired
    private PointSettingsService service;


    @GetMapping("/calculate-points")
    public Integer calculatePoints(@RequestParam BigDecimal totalAmount) {
        return service.calculatePoints(totalAmount);
    }

}