package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Wrist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NWristRepository extends JpaRepository<Wrist,Long> {

    List<Wrist> findAllByOrderByCreatedAtDesc();

    List<Wrist> findAllByStatusOrderByCreatedAtDesc(Integer status);
}
