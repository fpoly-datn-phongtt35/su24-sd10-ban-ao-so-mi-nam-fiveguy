package com.example.demo.repository.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OLRatingRepository2 extends JpaRepository<Rating, Long> {

    //    List<RatingEntity> findByBillDetailAndStatus(Product product, int status);

    List<Rating> findAllByCustomer_Id(Long Id);

    @Query("SELECT r FROM Rating r WHERE r.customer.id = :customerId ORDER BY r.createdAt DESC")
    Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId, Pageable pageable);

//    List<RatingEntity> findByProduct_Id(Long productId);

//    List<RatingEntity> findByBillDetail_Product_ProductId(Long idProduct);

    List<Rating> findByBillDetailAndStatus(BillDetail billDetail, int status);

    List<Rating> findByBillDetail_Id(Long idBillDetail);

    @Query("SELECT r FROM Rating r " +
            "JOIN r.billDetail bd " +
            "JOIN bd.productDetail pd " +
            "WHERE pd.product.id = :productId AND r.status = 2")
    Page<Rating> findByProductId(@Param("productId") Long productId, Pageable pageable);

    boolean existsByCustomerAndBillDetail(Customer customerEntity, BillDetail billDetail);

}
