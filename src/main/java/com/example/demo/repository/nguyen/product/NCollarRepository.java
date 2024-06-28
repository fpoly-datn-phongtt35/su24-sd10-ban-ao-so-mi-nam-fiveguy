package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Collar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NCollarRepository extends JpaRepository<Collar,Long> {

    List<Collar> findAllByOrderByCreatedAtDesc();

    List<Collar> findAllByStatusOrderByCreatedAtDesc(Integer status);
}
