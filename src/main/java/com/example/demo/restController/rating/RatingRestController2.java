package com.example.demo.restController.rating;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.OlRatingResponse;
import com.example.demo.security.service.SCAccountService;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.onlineShop.OlRatingService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/admin")
public class RatingRestController2 {


    @Autowired
    private OlRatingService2 olRatingService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @Autowired
    private SCAccountService accountService;

    // Trong controller của bạn
    @DeleteMapping("/deleteRate/{id}")
    public void deleteRateById(@PathVariable Long id) {
        // Gọi service để xóa địa chỉ với ID tương ứng
        olRatingService.deleteRating(id);
    }


    @PostMapping("/update")
    public void updateRating(@RequestHeader("Authorization") String token,@RequestBody Rating rating) {
        Optional<String> fullName = accountService.getFullNameByToken(token);
        rating.setReviewedAt(new Date());
        rating.setReviewer(fullName.get());
        olRatingService.updateRating(rating);
    }




    @PostMapping("/addRate")
    public ResponseEntity<?> addRate(@RequestHeader("Authorization") String token, @RequestBody OlRatingResponse ratingEntity) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            ratingEntity.setCustomer(customer.get());
            Integer result = olRatingService.addRating(ratingEntity);
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok(0);
    }


    @GetMapping("/ratings")
    public Page<Rating> getRatings(@RequestParam(required = false) Integer approvalStatus,
                                   @RequestParam(required = false, defaultValue = "") String search,
                                   @RequestParam int page,
                                   @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return olRatingService.getRatingsAdmin(approvalStatus, search, pageable);
    }



}