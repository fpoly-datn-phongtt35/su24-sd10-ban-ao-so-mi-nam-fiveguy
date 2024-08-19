package com.example.demo.repository.thuong;

import com.example.demo.entity.Collar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollarRepositoryTH extends JpaRepository<Collar, Long> {
    @Query("SELECT c FROM Collar c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (:status IS NULL OR c.status = :status)")
    Page<Collar> findByNameContainingIgnoreCaseAndStatus(String name, Integer status,Pageable pageable);
    @Query("SELECT c FROM Collar c WHERE :status IS NULL OR c.status = :status")
    Page<Collar> findAllAndStatus(@Param("status") Integer status, Pageable pageable);
    List<Collar> findAllByStatus(Integer status);
    Collar findByName(String name);
}
