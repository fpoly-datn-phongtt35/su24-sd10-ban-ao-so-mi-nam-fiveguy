package com.example.demo.repository.thuong;

import com.example.demo.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepositoryTH extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findAllByProduct_Id(Long id);

    @Query(value = "SELECT pd FROM ProductDetail pd JOIN pd.product p JOIN pd.color c WHERE pd.status = 1 AND (p.name LIKE %:keyword% OR c.name LIKE %:keyword% OR p.code LIKE %:keyword% OR CONCAT(p.name, ' ', c.name) LIKE %:keyword%)")
    List<ProductDetail> findAllByStatus(@Param("keyword") String keyword);


}
