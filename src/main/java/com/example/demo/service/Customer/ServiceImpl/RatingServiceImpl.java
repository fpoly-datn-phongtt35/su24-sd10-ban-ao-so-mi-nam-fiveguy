package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.Rating;
import com.example.demo.repository.Customer.RatingRepository;
import com.example.demo.service.Customer.AccountService;
import com.example.demo.service.Customer.CustomerService;
import com.example.demo.service.Customer.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    public void updateStatusRatingXacNhan(Long id){
        ratingRepository.updateStatusRatingXacNhan(id);
    }
    @Override
    public void updateStatusRatingHuy(Long id){
        ratingRepository.updateStatusRatingHuy(id);
    }

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getRatingById(Long id) {
        return ratingRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Rating> getAllRatingPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 1);
        return ratingRepository.findAll(pageable);
    }

    @Override
    public Rating createRating(Rating ratingEntity) {
        return ratingRepository.save(ratingEntity);
    }

    @Override
    public Rating updateRating(Rating ratingEntity, Long id) {
        Optional<Rating> existingRating = ratingRepository.findById(id);
        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setContent(ratingEntity.getContent());
            rating.setRate(ratingEntity.getRate());
            rating.setCreatedAt(ratingEntity.getCreatedAt());
            rating.setUpdatedAt(ratingEntity.getUpdatedAt());
            rating.setRated(ratingEntity.isRated());
            rating.setCustomer(ratingEntity.getCustomer());
            rating.setBillDetail(ratingEntity.getBillDetail());
            rating.setStatus(ratingEntity.getStatus());

            return ratingRepository.save(rating); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
//            return null;
        }
    }

    @Override
    public void deleteRating(Long id) {
        // Kiểm tra xem khách hàng có tồn tại trước khi xóa
        if (ratingRepository.existsById(id)) {
            ratingRepository.deleteById(id);
        } else {
            // Xử lý lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
        }
    }

//OL
@Autowired
private RatingRepository olRatingRepository;

    @Autowired
    private CustomerService olCustomerService;

    @Autowired
    private AccountService olAccountService;

//    @Autowired
//    private BillDetailService olBillDetailService;

//    @Autowired
//    private ProductDetailService olProductDetailService;

//    @Autowired
//    private BillDetailService billDetailService;

//    @Override
//    public List<Rating> findByProduct(Product productDetail) {
//        return olRatingRepository.findByBillDetailAndStatus(productDetail,1);
//    }

    @Override
    public List<Rating> findByProduct(Product productDetail) {
        return null;
    }

    @Override
    public List<Rating> getRatingListByUsername(String username) {
        return null;
    }

//    @Override
//    public List<Rating> getRatingListByUsername( String username) {
//        Optional<Account> account = olAccountService.findByAccount(username);
//
//        if (account.isPresent()) {
//            Optional<Customer> customerEntity = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
//            if (customerEntity.isPresent()) {
//                List<Rating> ratingEntities = olRatingRepository.findAllByCustomer_Id(customerEntity.get().getId());
//                return ratingEntities;
//
//            }
//        }
//        return null;
//
//    }

//    @Override
//    public void deleteRating(Long id) {
//        olRatingRepository.deleteById(id);
//    }

//    @Override
//    public boolean addRating(OlRatingResponse ratingEntity) {
//        Optional<BillDetail> billDetail = olBillDetailService.findById(ratingEntity.getIdBillDetail());
//        Optional<CustomerEntity> customerEntity = olCustomerService.findById(ratingEntity.getIdCustomer());
//
//        if (billDetail.isPresent() && customerEntity.isPresent()) {
//            boolean hasRated = olRatingRepository.existsByCustomerAndBillDetail(customerEntity.get(), billDetail.get());
//
//            if (!hasRated) {
//                Rating ratingEntity1 = new Rating();
//                ratingEntity1.setBillDetail(billDetail.get());
//                ratingEntity1.setContent(ratingEntity.getContent());
//                ratingEntity1.setCustomer(customerEntity.get());
//                ratingEntity1.setCreatedAt(new Date());
//                ratingEntity1.setRate(ratingEntity.getRate());
//                ratingEntity1.setRated(true);
//                ratingEntity1.setStatus(1);
//                olRatingRepository.save(ratingEntity1);
//                return true;
//            }
//        }
//
//        return false;
//    }

//    @Override
//    public List<Rating> findByProductId(Long productId) {
//        List<ProductDetail> productDetailList = olProductDetailService.findByProduct_Id(productId);
//        List<Rating> list = new ArrayList<>();
//
//        for (ProductDetail productDetail : productDetailList) {
//            List<BillDetail> billDetails = billDetailService.findByProductDetailAndStatus(productDetail.getId(), 1);
//            for (BillDetail billDetail : billDetails) {
//                List<Rating> ratingEntitiesForDetail = olRatingRepository.findByBillDetailAndStatus(billDetail,1);
//                        list.addAll(ratingEntitiesForDetail);
//            }
//        }
//        return list;
//    }


    @Override
    public List<Rating> findByBillDetailAndStatus(BillDetail billDetail,int status) {
        return olRatingRepository.findByBillDetailAndStatus(billDetail , status);
    }

    @Override
    public List<Rating> findByBillDetail_Id(Long idBillDetail) {
        return olRatingRepository.findByBillDetail_Id(idBillDetail);
    }

    @Override
    public Page<Rating> findAllByCustomer_IdOrderByYourFieldDesc(Long customerId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return olRatingRepository.findAllByCustomer_IdOrderByYourFieldDesc(customerId,pageRequest);
    }

    @Override
    public Page<Rating> findByProductId(Long productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return olRatingRepository.findByProductId(productId,pageRequest);
    }
// END OL
}
