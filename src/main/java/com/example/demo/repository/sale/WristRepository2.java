package com.example.demo.repository.sale;

import com.example.demo.entity.Wrist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface WristRepository2 extends JpaRepository<Wrist, Long> {

    List<Wrist> findByStatus(int status);


}
