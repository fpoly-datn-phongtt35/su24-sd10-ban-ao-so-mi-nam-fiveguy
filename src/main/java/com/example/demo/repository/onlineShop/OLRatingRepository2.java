package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLRatingRepository2 extends JpaRepository<Rating, Long>, JpaSpecificationExecutor<Rating> {

    List<Rating> findAllByCustomer_Id(Long Id);

    @Query("SELECT r FROM Rating r " +
            "JOIN r.billDetail bd " +
            "JOIN bd.bill b " +
            "JOIN bd.productDetail pd " +
            "JOIN pd.product p " +
            "WHERE r.customer.id = :customerId " +
            "AND (p.name LIKE %:search% OR b.code LIKE %:search%) " +
            "ORDER BY r.createdAt DESC")
    Page<Rating> findAllByCustomerIdAndSearch(
            @Param("customerId") Long customerId,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("SELECT r FROM Rating r WHERE r.billDetail = :billDetail AND r.status = 1 AND r.approvalStatus = 1")
    List<Rating> findByBillDetailAndStatus(@Param("billDetail") BillDetail billDetail);

    List<Rating> findByBillDetail_Id(Long idBillDetail);

    @Query("SELECT r FROM Rating r " +
            "JOIN r.billDetail bd " +
            "JOIN bd.productDetail pd " +
            "WHERE pd.product.id = :productId AND r.status = 1 AND r.approvalStatus = 1")
    Page<Rating> findByProductId(@Param("productId") Long productId, Pageable pageable);

    boolean existsByCustomerAndBillDetail(Customer customerEntity, BillDetail billDetail);

}
