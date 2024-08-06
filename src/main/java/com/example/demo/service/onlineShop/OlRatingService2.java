package com.example.demo.service.onlineShop;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.OlRatingResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OlRatingService2 {

    List<Rating> findByProduct(Product productDetail);
//    List<RatingEntity> findByProductId(Long id);

//    List<Rating> getRatingListByUsername( String username);

    void deleteRating(Long id);

    boolean addRating(OlRatingResponse rating);

//    List<RatingEntity> findByProduct_Id(Long productId);

    List<Rating> findByBillDetailAndStatus(BillDetail billDetail, int status);

    List<Rating> findByBillDetail_Id(Long idBillDetail);

    Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId, int page, int size);

    Page<Rating> findByProductId( Long productId, int page, int size);


}
