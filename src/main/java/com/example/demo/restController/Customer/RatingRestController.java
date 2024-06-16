package com.example.demo.restController.Customer;

import com.example.demo.entity.Rating;
import com.example.demo.service.Customer.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rating")
@CrossOrigin("*")

public class RatingRestController {
   @Autowired
   RatingService ratingService;

    @GetMapping("")
    public ResponseEntity<List<Rating>> getAllRating() {
        List<Rating> rating = ratingService.getAllRating();
        return ResponseEntity.ok(rating);
    }
    @PutMapping("/update-status-rating-xac-nhan/{id}")
    public void updateStatusRatingXacNhan(@PathVariable Long id){
        ratingService.updateStatusRatingXacNhan(id);
    }
    @PutMapping("/update-status-rating-huy/{id}")
    public void updateStatusRatingHuy(@PathVariable Long id){
        ratingService.updateStatusRatingHuy(id);
    }

//    @GetMapping("/pageall")
//    public ResponseEntity<Page<Rating>> getAllRatingPage(@RequestParam(defaultValue = "0", name = "page") Integer page) {
//        return ResponseEntity.ok(ratingService.getAllRatingPage(page));
//    }
//
//    @GetMapping("/findby/{ratingId}")
//    public ResponseEntity<Rating> getRatingById(@PathVariable Long ratingId) {
//        Rating rating = ratingService.getRatingById(ratingId);
//        if (rating != null) {
//            return ResponseEntity.ok(rating);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @PostMapping("")
    public ResponseEntity<?> createRating(@RequestBody Rating ratingEntity) {
        try {
            Rating createdRating = ratingService.createRating(ratingEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Rating> updateRating(@RequestBody Rating ratingEntity, @PathVariable Long ratingId) {
        Rating rating = ratingService.updateRating(ratingEntity, ratingId);
        if (rating != null) {
            return ResponseEntity.ok(rating);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        try {
            ratingService.deleteRating(ratingId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
