package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Favorite;
import com.example.demo.repository.Customer.FavoriteReposirotyH;
import com.example.demo.service.Customer.AccountServiceH;
import com.example.demo.service.Customer.CustomerServiceH;
import com.example.demo.service.Customer.FavoriteServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceHImpl implements FavoriteServiceH {

    private final FavoriteReposirotyH favoriteRepository;

    @Autowired
    public FavoriteServiceHImpl(FavoriteReposirotyH favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public List<Favorite> getAllFavorite() {
        return favoriteRepository.findAll();
    }

    @Override
    public Favorite getFavoriteById(Long id) {
        return favoriteRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Favorite> getAllFavoritePage(Integer page) {
        Pageable pageable = PageRequest.of(page, 1);
        return favoriteRepository.findAll(pageable);
    }

    @Override
    public Favorite createFavorite(Favorite favoriteEntity) {
        return favoriteRepository.save(favoriteEntity);
    }

    @Override
    public Favorite updateFavorite(Favorite favoriteEntity, Long id) {
        Optional<Favorite> existingFavorite = favoriteRepository.findById(id);
        if (existingFavorite.isPresent()) {
            Favorite favorite = existingFavorite.get();
            favorite.setCustomer(favoriteEntity.getCustomer());
            favorite.setProductDetail(favoriteEntity.getProductDetail());
            favorite.setCreatedAt(favoriteEntity.getCreatedAt());
            favorite.setUpdatedAt(favoriteEntity.getUpdatedAt());
            favorite.setStatus(favoriteEntity.getStatus());

            return favoriteRepository.save(favorite); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
//            return null;
        }
    }

    @Override
    public void deleteFavorite(Long id) {
        // Kiểm tra xem khách hàng có tồn tại trước khi xóa
        if (favoriteRepository.existsById(id)) {
            favoriteRepository.deleteById(id);
        } else {
            // Xử lý lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
        }
    }

//OL

//    @Autowired
//    private OLAddressRepository repository;

    @Autowired
    private FavoriteReposirotyH olFavoritesRepository;

    @Autowired
    private AccountServiceH olAccountServiceH;

    @Autowired
    private CustomerServiceH olCustomerServiceH;

////    @Autowired
////    private ImageService olImageService;
////
////    @Autowired
////    private ProductService olProductService;
////
////    @Autowired
////    private ProductDetailService olProductDetailService;
////
////    @Override
////    public List<AddressEntity> getAddressListByUsername(String username) {
////        Optional<AccountEntity> account = olAccountServiceH.findByAccount(username);
////
////        if (account.isPresent()) {
////            // Lấy thông tin khách hàng từ tài khoản
////            Optional<CustomerEntity> customerEntity = Optional.ofNullable(olCustomerServiceH.findByAccount_Id(account.get().getId()));
////            if (customerEntity.isPresent()) {
////                return repository.findAllByCustomer_IdAndStatus(customerEntity.get().getId(),1);
////            }
////        }
////        return Collections.emptyList(); // Trả về danh sách trống nếu không tìm thấy thông tin khách hàng hoặc địa chỉ
////    }
////
////    @Override
////    public void deleteAddress(Long id) {
////        repository.deleteById(id);
////
////    }
////
////    @Override
////    public boolean update(AddressEntity addressRequest) {
////        if (addressRequest.getDefaultAddress()) {
////            // Lấy danh sách địa chỉ của khách hàng
////            List<AddressEntity> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
////            for (AddressEntity address : customerAddresses) {
////                if (!address.getId().equals(addressRequest.getId())) {
////                    address.setDefaultAddress(false);
////                    repository.save(address);
////                }
////            }
////        }
////
////        addressRequest.setUpdatedAt(new Date());
////        repository.save(addressRequest);
////        return true;
////    }
////
////    @Override
////    public boolean addAddress(AddressEntity addressRequest) {
////        if (addressRequest.getDefaultAddress()) {
////            // Lấy danh sách địa chỉ của khách hàng
////            List<AddressEntity> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
////            for (AddressEntity address : customerAddresses) {
////                if (!address.getId().equals(addressRequest.getId())) {
////                    address.setDefaultAddress(false);
////                    repository.save(address);
////                }
////            }
////        }
////        addressRequest.setCreatedAt(new Date());
////        addressRequest.setStatus(1);
////        addressRequest.setId(null);
////        repository.save(addressRequest);
////        return true;
////    }
////
////
////    @Override
////    public List<OlFavoritesResponse> getFavoriteListByUsername(String username) {
////        Optional<AccountEntity> account = olAccountServiceH.findByAccount(username);
////        List<OlFavoritesResponse> favoritesResponses = new ArrayList<>();
////
////        if (account.isPresent()) {
////            Optional<CustomerEntity> customerEntity = Optional.ofNullable(olCustomerServiceH.findByAccount_Id(account.get().getId()));
////            if (customerEntity.isPresent()) {
////                List<FavoriteEntity> favoriteEntities = olFavoritesRepository.findAllByCustomer_IdAndStatus(customerEntity.get().getId(), 1);
////
////                for (FavoriteEntity favoriteEntity : favoriteEntities) {
////                    OlFavoritesResponse favoritesResponse = new OlFavoritesResponse();
////                    favoritesResponse.setId(favoriteEntity.getId());
////                    favoritesResponse.setCustomer(favoriteEntity.getCustomer());
////                    favoritesResponse.setProduct(favoriteEntity.getProduct());
////                    favoritesResponse.setCreatedAt(favoriteEntity.getCreatedAt());
////                    favoritesResponse.setUpdatedAt(favoriteEntity.getUpdatedAt());
////                    favoritesResponse.setStatus(favoriteEntity.getStatus());
////
////                    List<ProductDetail> productDetailList = olProductDetailService.findByProduct(favoriteEntity.getProduct());
////                    String imagePath = null;
////                    for (ProductDetail productDetail : productDetailList) {
////                        List<Image> images = olImageService.findByProductDetailId(productDetail.getId());
////                        if (!images.isEmpty()) {
////                            String currentPath = images.get(0).getPath();
////                            if (currentPath != null) {
////                                imagePath = currentPath;
////                                break; // Kết thúc vòng lặp nếu đã tìm thấy ảnh không null
////                            }
////                        }
////                    }
////
////                    if (imagePath != null) {
////                        favoritesResponse.setPath(imagePath);
////                    }
////
////
////                    BigDecimal price = null;
////
////                    for (ProductDetail productDetail : productDetailList) {
////                        if (productDetail.getPrice() != null) {
////                            price = productDetail.getPrice();
////                            break; // Kết thúc vòng lặp nếu đã tìm thấy giá không null
////                        }
////                    }
////
////                    if (price != null) {
////                        favoritesResponse.setPrice(price);
////                    }
////
////                    favoritesResponses.add(favoritesResponse);
////                }
////            }
////        }
////        return favoritesResponses;
////    }


//    @Override
//    public void deleteFavorite(Long id) {
//        olFavoritesRepository.deleteById(id);
//    }


//    @Override
//    public Integer addFavorite(OlFavoritesAddResponse favoriteEntity) {
//        FavoriteEntity favoriteEntity1 = new FavoriteEntity();
//
//        Long idProduct = favoriteEntity.getIdProduct();
//
//        // Kiểm tra xem idProduct đã tồn tại trong cơ sở dữ liệu chưa
//        Optional<Product> product = olProductService.findById(idProduct);
//        if (product.isPresent()) {
//            // Kiểm tra xem sản phẩm đã tồn tại trong danh sách yêu thích của khách hàng chưa
//            CustomerEntity customer = favoriteEntity.getCustomer();
//            boolean isProductAlreadyInFavorites = olFavoritesRepository.existsByCustomerAndProduct(customer, product.get());
//
//            if (!isProductAlreadyInFavorites) {
//                favoriteEntity1.setProduct(product.get());
//                favoriteEntity1.setCustomer(customer);
//                favoriteEntity1.setCreatedAt(new Date());
//                favoriteEntity1.setStatus(favoriteEntity.getStatus());
//
//                olFavoritesRepository.save(favoriteEntity1);
//                return 1; // Trả về 1 nếu thành công
//            } else {
//                return 2; // Trả về 2 nếu sản phẩm đã tồn tại trong danh sách yêu thích của khách hàng
//            }
//        }
//        return 0; // Trả về 0 nếu idProduct không tồn tại trong cơ sở dữ liệu
//    }

//    @Override
//    public Page<OlFavoritesResponse> findAllByCustomer(Long customerId, int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        Page<FavoriteEntity> favoriteEntityPage = olFavoritesRepository.findAllByCustomer(customerId, pageRequest);
//
//        List<OlFavoritesResponse> favoritesResponses = favoriteEntityPage.getContent().stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(favoritesResponses, pageRequest, favoriteEntityPage.getTotalElements());
//    }
//
//    private OlFavoritesResponse convertToResponse(FavoriteEntity favoriteEntity) {
//        OlFavoritesResponse favoritesResponse = new OlFavoritesResponse();
//        favoritesResponse.setId(favoriteEntity.getId());
//        favoritesResponse.setCustomer(favoriteEntity.getCustomer());
//        favoritesResponse.setProduct(favoriteEntity.getProduct());
//        favoritesResponse.setCreatedAt(favoriteEntity.getCreatedAt());
//        favoritesResponse.setUpdatedAt(favoriteEntity.getUpdatedAt());
//        favoritesResponse.setStatus(favoriteEntity.getStatus());
//
//        List<ProductDetail> productDetailList = olProductDetailService.findByProduct(favoriteEntity.getProduct());
//        String imagePath = null;
//        for (ProductDetail productDetail : productDetailList) {
//            List<Image> images = olImageService.findByProductDetailId(productDetail.getId());
//            if (!images.isEmpty()) {
//                String currentPath = images.get(0).getPath();
//                if (currentPath != null) {
//                    imagePath = currentPath;
//                    break;
//                }
//            }
//        }
//
//        if (imagePath != null) {
//            favoritesResponse.setPath(imagePath);
//        }
//
//        BigDecimal price = null;
//
//        for (ProductDetail productDetail : productDetailList) {
//            if (productDetail.getPrice() != null) {
//                price = productDetail.getPrice();
//                break;
//            }
//        }
//
//        if (price != null) {
//            favoritesResponse.setPrice(price);
//        }
//
//        return favoritesResponse;
//    }
// END OL
}
