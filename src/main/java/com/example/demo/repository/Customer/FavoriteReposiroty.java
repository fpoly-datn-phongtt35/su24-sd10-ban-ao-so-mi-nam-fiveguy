package com.example.demo.repository.Customer;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteReposiroty  extends JpaRepository<Favorite, Long> {
    @Query("SELECT f FROM Favorite f WHERE f.customer.id = :customerId AND f.productDetail.id = :productDetail")
    List<Favorite> findByCustomerAndProductDetail(
            @Param("customerId") Long customerId,
            @Param("productDetail") Long product
    );
//OL
    List<Favorite> findAllByCustomer_IdAndStatus(Long Id,int status);

    boolean existsByCustomerAndProductDetail(Customer customer, ProductDetail productDetail);

//    Page<FavoriteEntity> findAllByCustomerEntity_IdOrderByCreatedAtDesc(Long customerId, Pageable pageable);


    @Query("SELECT b FROM Favorite b WHERE b.customer.id = :customerId ORDER BY b.createdAt DESC")
    Page<Favorite> findAllByCustomer(@Param("customerId") Long customerId, Pageable pageable);
//END OL
}
