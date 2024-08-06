package com.example.demo.restController.onlineShop;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Rating;
import com.example.demo.model.response.onlineShop.OlRatingResponse;
import com.example.demo.security.service.SCCustomerService;
import com.example.demo.service.onlineShop.OlRatingService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/home")
public class OlRatingRestController2 {


    @Autowired
    private OlRatingService2 olRatingService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @GetMapping("/rates")
    public ResponseEntity<?> listRates(@RequestHeader("Authorization") String token,
                                       @RequestParam("page") int page) {

        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
            if (customer.isPresent()) {
                int size = 6;
                Page<Rating> ratingEntities = olRatingService.findAllByCustomer_IdOrderByYourFieldDesc(customer.get().getId(), page, size);
                if (ratingEntities.isEmpty()) {
                    return ResponseEntity.ok("Không có hóa đơn nào được tìm thấy cho khách hàng này");
                }
                return ResponseEntity.ok(ratingEntities);
            }

        return ResponseEntity.notFound().build();
    }

    // Trong controller của bạn
    @DeleteMapping("/deleteRate/{id}")
    public void deleteRateById(@PathVariable Long id) {
        // Gọi service để xóa địa chỉ với ID tương ứng
        olRatingService.deleteRating(id);
    }


    @PostMapping("/addRate")
    public boolean addRate(@RequestHeader("Authorization") String token,@RequestBody OlRatingResponse ratingEntity) {
        Optional<Customer> customer = SCCustomerService.getCustomerByToken(token);
        if (customer.isPresent()) {
            ratingEntity.setCustomer(customer.get());
            return olRatingService.addRating(ratingEntity);

        }
        return false;
    }


    @GetMapping("/listRate")
    public ResponseEntity<?> getRatingsByProductId(@RequestParam("productId") Long productId,
                                                   @RequestParam("page") int page) {
        int size = 6;
        return ResponseEntity.ok(olRatingService.findByProductId(productId,page,size));
    }

}