package com.example.demo.service.onlineShop.impl;


import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Product;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.OlRatingResponse;
import com.example.demo.repository.onlineShop.OLBillDetailRepository2;
import com.example.demo.repository.onlineShop.OLRatingRepository2;
import com.example.demo.service.onlineShop.OLBillDetailService2;
import com.example.demo.service.onlineShop.OLProductDetailService2;
import com.example.demo.service.onlineShop.OlRatingService2;
import com.example.demo.service.onlineShop.RatingSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OLRatingServiceImpl2 implements OlRatingService2 {


    @Autowired
    private OLRatingRepository2 olRatingRepository;



    @Autowired
    private OLBillDetailRepository2 olBillDetailRepository2;


    @Override
    public List<Rating> findByProduct(Product productDetail) {
        return null;
    }

//    @Override
//    public List<Rating> getRatingListByUsername( String username) {
//        Optional<AccountEntity> account = olAccountService.findByAccount(username);
//
//        if (account.isPresent()) {
//            Optional<CustomerEntity> customerEntity = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
//            if (customerEntity.isPresent()) {
//                List<RatingEntity> ratingEntities = olRatingRepository.findAllByCustomer_Id(customerEntity.get().getId());
//                return ratingEntities;
//
//            }
//        }
//        return null;
//
//    }

    @Override
    public void deleteRating(Long id) {
        olRatingRepository.deleteById(id);
    }

    @Override
    public void hideRating(Long id) {
        Optional<Rating> rating = olRatingRepository.findById(id);
        if (rating.isPresent()){
            rating.get().setStatus(3);
            olRatingRepository.save(rating.get());
        }
    }

    @Override
    public Integer updateRating(Rating rating) {
        olRatingRepository.save(rating);
        return 0;
    }

    @Override
    public Integer addRating(OlRatingResponse ratingEntity) {
        Optional<BillDetail> billDetail = olBillDetailRepository2.findById(ratingEntity.getIdBillDetail());

        if (billDetail.isPresent() ) {
            boolean hasRated = olRatingRepository.existsByCustomerAndBillDetail(ratingEntity.getCustomer(), billDetail.get());

            if (!hasRated) {
                Rating ratingEntity1 = new Rating();
                ratingEntity1.setBillDetail(billDetail.get());
                ratingEntity1.setContent(ratingEntity.getContent());
                ratingEntity1.setCustomer(ratingEntity.getCustomer());
                ratingEntity1.setCreatedAt(new Date());
                ratingEntity1.setRate(ratingEntity.getRate());
                ratingEntity1.setRated(true);
                ratingEntity1.setStatus(1);
                ratingEntity1.setApprovalStatus(2);
                olRatingRepository.save(ratingEntity1);
                return 1;
            }
        }

        return 0;
    }


    @Override
    public List<Rating> findByBillDetail(BillDetail billDetail) {
        return olRatingRepository.findByBillDetailAndStatus(billDetail );
    }

    @Override
    public List<Rating> findByBillDetail_Id(Long idBillDetail) {
        return olRatingRepository.findByBillDetail_Id(idBillDetail);
    }

    @Override
    public Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId,String search, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Rating> ratings = olRatingRepository.findAllByCustomerIdAndSearch(customerId,search,pageRequest);
        return olRatingRepository.findAllByCustomerIdAndSearch(customerId,search,pageRequest);
    }

    @Override
    public Page<Rating> findByProductId(Long productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return olRatingRepository.findByProductId(productId,pageRequest);
    }

    @Override
    public Page<Rating> getRatingsAdmin(int approvalStatus, String search, Pageable pageable) {
        Specification<Rating> spec = Specification.where(RatingSpecifications.hasApprovalStatus(approvalStatus))
                .and(RatingSpecifications.hasSearch(search));
        return olRatingRepository.findAll(spec, pageable);
    }

}