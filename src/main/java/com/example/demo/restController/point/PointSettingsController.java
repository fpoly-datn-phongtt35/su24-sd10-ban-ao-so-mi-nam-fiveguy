package com.example.demo.restController.point;

import com.example.demo.entity.PointSettings;
import com.example.demo.service.point.PointSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin/point-settings")
public class PointSettingsController {

    @Autowired
    private PointSettingsService service;


    @GetMapping
    public ResponseEntity<PointSettings> getPointSettings() {
        PointSettings pointSettings = service.getPointSettings();
        return new ResponseEntity<>(pointSettings, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PointSettings> updatePointSettings(@RequestBody PointSettings pointSettings) {
        try {
            PointSettings updatedPointSettings = service.updatePointSettings(pointSettings);
            return new ResponseEntity<>(updatedPointSettings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}