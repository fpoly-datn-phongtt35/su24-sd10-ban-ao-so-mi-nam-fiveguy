package com.example.demo.service.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.OlRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OlRatingService2 {

    List<Rating> findByProduct(Product productDetail);
//    List<RatingEntity> findByProductId(Long id);

//    List<Rating> getRatingListByUsername( String username);

    void deleteRating(Long id);


    void hideRating(Long id);

    Integer updateRating(Rating rating);

    Integer addRating(OlRatingResponse rating);

//    List<RatingEntity> findByProduct_Id(Long productId);

    List<Rating> findByBillDetail(BillDetail billDetail);

    List<Rating> findByBillDetail_Id(Long idBillDetail);

    Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId, String search,int page, int size);

    Page<Rating> findByProductId( Long productId, int page, int size);

    Page<Rating> getRatingsAdmin(int approvalStatus, String search, Pageable pageable);

}
