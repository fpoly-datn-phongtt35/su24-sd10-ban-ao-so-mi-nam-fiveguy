package com.example.demo.service.point;

import com.example.demo.entity.PointSettings;

public interface PointSettingsService {
    PointSettings getPointSettings();
    PointSettings updatePointSettings(PointSettings pointSettings);
}