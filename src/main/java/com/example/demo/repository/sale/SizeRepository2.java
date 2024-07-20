package com.example.demo.repository.sale;

import com.example.demo.entity.Collar;
import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SizeRepository2 extends JpaRepository<Size, Long> {

    List<Size> findByStatus(int status);

}