package com.example.demo.repository.Customer;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepostitoryH extends JpaRepository<Image, Long> {

//    List<Image> findAllByProductId(Long id);
}
