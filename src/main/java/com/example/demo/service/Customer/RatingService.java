package com.example.demo.service.Customer;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.Rating;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RatingService {
    List<Rating> getAllRating();

//    Page<Rating> getAll(Integer page);

    Rating getRatingById(Long id);

    Page<Rating> getAllRatingPage(Integer page);

    Rating createRating(Rating ratingEntity);

    Rating updateRating(Rating ratingEntity, Long id);

    void deleteRating(Long id);

    void updateStatusRatingXacNhan(Long id);
    void updateStatusRatingHuy(Long id);

////OL

    List<Rating> findByProduct(Product productDetail);
// //   List<RatingEntity> findByProductId(Long id);

    List<Rating> getRatingListByUsername( String username);

// //   void deleteRating(Long id);

//    boolean addRating(RatingResponse ratingEntity);

//  //  List<RatingEntity> findByProduct_Id(Long productId);

    List<Rating> findByBillDetailAndStatus(BillDetail billDetail, int status);

    List<Rating> findByBillDetail_Id(Long idBillDetail);

    Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId, int page, int size);

    Page<Rating> findByProductId( Long productId, int page, int size);
// // END OL
}
