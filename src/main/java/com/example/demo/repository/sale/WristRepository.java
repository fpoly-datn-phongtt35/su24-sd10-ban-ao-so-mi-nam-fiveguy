package com.example.demo.repository.sale;

import com.example.demo.entity.Wrist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface WristRepository extends JpaRepository<Wrist, Long> {
}
