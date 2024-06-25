package com.example.demo.repository.Customer;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Modifying
    @Transactional
    @Query("Update Rating r Set r.status = 2 where  r.id = :id")
    void updateStatusRatingXacNhan( Long id); @Modifying
    @Transactional
    @Query("Update Rating r Set r.status = 3 where  r.id = :id")
    void updateStatusRatingHuy(Long id);

//OL

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

    boolean existsByCustomerAndBillDetail(Customer customer, BillDetail billDetail);
//END OL
}
