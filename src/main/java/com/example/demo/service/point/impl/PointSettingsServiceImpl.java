package com.example.demo.service.point.impl;

import com.example.demo.entity.PointSettings;
import com.example.demo.repository.point.PointSettingsRepository;
import com.example.demo.service.point.PointSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointSettingsServiceImpl implements PointSettingsService {

    @Autowired
    private PointSettingsRepository repository;



    @Override
    public PointSettings getPointSettings() {
        // Fetch the single record from the database
        PointSettings settings = repository.findFirstByOrderByIdAsc();
        if (settings == null) {
            // If no record exists, create a new one with default values
            settings = new PointSettings();
            settings.setPointsPerAmount(10000); // Set default value for points per amount
            settings = repository.save(settings);
        }
        return settings;
    }

    @Override
    public PointSettings updatePointSettings(PointSettings pointSettings) {
        // Check if the record exists before updating
        PointSettings existingSettings = getPointSettings();
        if (existingSettings != null) {
            existingSettings.setPointsPerAmount(pointSettings.getPointsPerAmount());
            return repository.save(existingSettings);
        } else {
            throw new IllegalArgumentException("PointSettings record not found");
        }
    }
}