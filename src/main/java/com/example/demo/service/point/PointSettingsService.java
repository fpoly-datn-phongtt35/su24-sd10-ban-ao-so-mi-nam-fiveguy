package com.example.demo.service.point;

import com.example.demo.entity.PointSettings;

import java.math.BigDecimal;

public interface PointSettingsService {
    PointSettings getPointSettings();
    PointSettings updatePointSettings(PointSettings pointSettings);

    Integer calculatePoints(BigDecimal totalAmount);
}