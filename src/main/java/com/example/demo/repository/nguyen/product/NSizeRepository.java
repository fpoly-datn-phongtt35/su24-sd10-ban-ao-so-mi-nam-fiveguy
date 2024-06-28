package com.example.demo.repository.nguyen.product;

import com.example.demo.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NSizeRepository extends JpaRepository<Size,Long> {

    List<Size> findAllByOrderByCreatedAtDesc();

    List<Size> findAllByStatusOrderByCreatedAtDesc(Integer status);
}
