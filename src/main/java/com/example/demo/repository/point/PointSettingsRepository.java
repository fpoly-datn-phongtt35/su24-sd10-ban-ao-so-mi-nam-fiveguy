package com.example.demo.repository.point;

import com.example.demo.entity.PointSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointSettingsRepository extends JpaRepository<PointSettings, Long> {
    // Custom query to get the single record, if necessary
    PointSettings findFirstByOrderByIdAsc();
}