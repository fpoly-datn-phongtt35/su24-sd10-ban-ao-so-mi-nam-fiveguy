package com.example.demo.service.onlineShop.impl;

import com.example.demo.entity.BillDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.response.onlineShop.ProductDetailsDTO;
import com.example.demo.model.response.onlineShop.ProductInfoDTO;
import com.example.demo.model.response.onlineShop.ProductSaleDetails;
import com.example.demo.repository.onlineShop.OLProductRepository2;
import com.example.demo.service.onlineShop.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OLProductServiceImpl2 implements OLProductService2 {


    @Autowired
    private OLProductRepository2 productRepository;

    @Autowired
    private OLColorService2 olColorService2;

    @Autowired
    private OLSizeService2 olSizeService2;

    @Autowired
    private OLProductDetailService2 olProductDetailService2;

    @Autowired
    private OLBillDetailServiceImpl2 olBillDetailServiceImpl2;

    @Autowired
    private OLImageService2 olImageService2;

    // Phương thức để lọc sản phẩm dựa trên các thuộc tính và phân trang, sắp xếp



    public List<ProductSaleDetails> getProduct() {
        List<Object[]> results = productRepository.findProductsWithImages();

        List<ProductSaleDetails> productSaleDetails = results.stream().map(result -> {
            Long productId = ((Number) result[0]).longValue();
            String productName = (String) result[1];
            BigDecimal productPrice = (BigDecimal) result[2];
            Integer discountPrice = (Integer) result[3];
            Integer promotionalPrice = (Integer) result[4];
            Integer saleValue = (Integer) result[5];
            Integer discountType = (Integer) result[6];
            String imagePath = (String) result[7];

            // Retrieve product object using findById
            Product product = productRepository.findById(productId)
                    .orElse(null); // Handle if product is not found

            return new ProductSaleDetails(
                    productId,
                    productName,
                    productPrice,
                    discountPrice,
                    promotionalPrice,
                    saleValue,
                    discountType,
                    imagePath,
                    product // Pass the retrieved product object
            );
        }).collect(Collectors.toList());

        return productSaleDetails;
    }





    @Override
    public Page<ProductSaleDetails> filterProducts(
            Long categoryId, String name, Long colorName, Long sizeName, Long materialName,
            Long collarName, Long wristName, Pageable pageable) {

        List<ProductSaleDetails> productSaleDetailsList = getProduct();

        List<ProductSaleDetails> filteredProductSaleDetails = productSaleDetailsList.stream()
                .filter(productSaleDetails -> {
                    Product product = productSaleDetails.getProduct();
                    return (categoryId == null || product.getCategory().getId().equals(categoryId)) &&
                            (name == null || name.isEmpty() || product.getName().toLowerCase().contains(name.toLowerCase()) ||
                                    product.getCode().toLowerCase().contains(name.toLowerCase())) &&
                            (collarName == null || product.getCollar().getId().equals(collarName)) &&
                            (wristName == null || product.getWrist().getId().equals(wristName)) &&
                            (colorName == null || product.getProductDetails().stream().anyMatch(pd -> pd.getColor().getId().equals(colorName))) &&
                            (sizeName == null || product.getProductDetails().stream().anyMatch(pd -> pd.getSize().getId().equals(sizeName))) &&
                            (materialName == null || product.getMaterial().getId().equals(materialName)) &&
                            product.getStatus().equals(1); // Assuming the status filter
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProductSaleDetails.size());
        List<ProductSaleDetails> output = filteredProductSaleDetails.subList(start, end);

        return new PageImpl<>(output, pageable, filteredProductSaleDetails.size());
    }




    @Override
    public Page<ProductSaleDetails> filterProducts2(
            Set<Long> categoryIds, Set<Long> collarIds, Set<Long> wristIds,
            Set<Long> colorIds, Set<Long> sizeIds, Set<Long> materialIds,
            String searchTerm, Pageable pageable) {

        List<ProductSaleDetails> productSaleDetailsList = getProduct();

        List<ProductSaleDetails> filteredProductSaleDetails = productSaleDetailsList.stream()
                .filter(productSaleDetails -> {
                    Product product = productSaleDetails.getProduct();

                    boolean categoryMatch = (categoryIds == null || categoryIds.isEmpty() || categoryIds.contains(product.getCategory().getId()));
                    boolean collarMatch = (collarIds == null || collarIds.isEmpty() || collarIds.contains(product.getCollar().getId()));
                    boolean wristMatch = (wristIds == null || wristIds.isEmpty() || wristIds.contains(product.getWrist().getId()));
                    boolean materialMatch = (materialIds == null || materialIds.isEmpty() || materialIds.contains(product.getMaterial().getId()));
                    boolean statusMatch = product.getStatus().equals(1); // Assuming the status filter

                    boolean colorMatch = (colorIds == null || colorIds.isEmpty() ||
                            product.getProductDetails().stream().anyMatch(pd -> colorIds.contains(pd.getColor().getId())));
                    boolean sizeMatch = (sizeIds == null || sizeIds.isEmpty() ||
                            product.getProductDetails().stream().anyMatch(pd -> sizeIds.contains(pd.getSize().getId())));

                    boolean searchTermMatch = (searchTerm == null || searchTerm.isEmpty() ||
                            product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                            product.getCode().toLowerCase().contains(searchTerm.toLowerCase()));

                    return categoryMatch && collarMatch && wristMatch && colorMatch && sizeMatch && materialMatch && searchTermMatch && statusMatch;
                })
                .collect(Collectors.toList());

        // Sắp xếp danh sách dựa trên promotionalPrice nếu được yêu cầu
        Sort.Order sortOrder = pageable.getSort().getOrderFor("promotionalPrice");
        if (sortOrder != null) {
            Comparator<ProductSaleDetails> priceComparator = Comparator.comparing(psd -> {
                Integer promoPrice = psd.getPromotionalPrice();
                BigDecimal price = psd.getProductPrice();
                return promoPrice == null || promoPrice == 0 ? price : BigDecimal.valueOf(promoPrice);
            }, Comparator.nullsLast(Comparator.naturalOrder())); // xử lý null
            if (sortOrder.getDirection() == Sort.Direction.DESC) {
                priceComparator = priceComparator.reversed();
            }
            filteredProductSaleDetails.sort(priceComparator);
        } else {
            // Sắp xếp danh sách theo createdAt nếu không có sortDir hoặc sort khác price
            Comparator<ProductSaleDetails> createdAtComparator = Comparator.comparing(psd -> psd.getProduct().getCreatedAt(), Comparator.nullsLast(Comparator.naturalOrder()));
            Sort.Order createdAtOrder = pageable.getSort().getOrderFor("createdAt");
            if (createdAtOrder != null && createdAtOrder.getDirection() == Sort.Direction.DESC) {
                createdAtComparator = createdAtComparator.reversed();
            }
            filteredProductSaleDetails.sort(createdAtComparator);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProductSaleDetails.size());
        List<ProductSaleDetails> output = filteredProductSaleDetails.subList(start, end);

        return new PageImpl<>(output, pageable, filteredProductSaleDetails.size());
    }
    private ProductInfoDTO convertToProductInfoDTO(Object[] productInfoArray) {
        if (productInfoArray != null && productInfoArray.length > 0) {
            Long productId = (productInfoArray[0] instanceof Long) ? (Long) productInfoArray[0] : null;
            String productName = (String) productInfoArray[1];
            BigDecimal price = (BigDecimal) productInfoArray[2];
            String wristName = (String) productInfoArray[3];
            String materialName = (String) productInfoArray[4];
            String categoryName = (String) productInfoArray[5];
            String collarName = (String) productInfoArray[6];
            Integer promotionalPrice = (Integer) productInfoArray[7];
            Integer discountType = (Integer) productInfoArray[8];
            Integer value = (Integer) productInfoArray[9];
            String brandName = (String) productInfoArray[10];
            String supplierName = (String) productInfoArray[11];

            return new ProductInfoDTO(productId, productName, price, wristName, materialName, categoryName, collarName, promotionalPrice, discountType,value,getTotalQuantitySold(productId),brandName,supplierName);
        }
        return null;
    }

    @Override
    public ProductDetailsDTO getProductDetails(Long idProduct) {
        List<Object[]> productInfoArray = productRepository.getProductInfo(idProduct);
        ProductInfoDTO productInfo = convertToProductInfoDTO(!productInfoArray.isEmpty() ? productInfoArray.get(0) : null);

        List<Object[]> colors = olColorService2.getColorsByProductId(idProduct);
        List<Object[]> sizes = olSizeService2.getSizesByProductId(idProduct);


        Long idColorFirst = colors.isEmpty() ? null : (Long) colors.get(0)[0];

        List<String> listImage = colors.isEmpty() ? Collections.emptyList()
                : olImageService2.getImagesByProductIdAndColorId(idProduct, (idColorFirst));

        return new ProductDetailsDTO(productInfo, colors, sizes,listImage,0);
    }




    private int getTotalQuantitySold(Long idProduct) {
        Optional<Product> product1 = productRepository.findById(idProduct);
        if (product1.isPresent()){
            List<ProductDetail> productDetails = olProductDetailService2.findByProduct(product1.get());
            int totalQuantity = 0;
            for (ProductDetail detail : productDetails) {
                List<BillDetail> billDetails = olBillDetailServiceImpl2.findByProductDetail(detail);
                for (BillDetail billDetail : billDetails) {
                    if (billDetail.getBill().getStatus() == 3){
                        totalQuantity += billDetail.getQuantity();
                    }
                }
            }
            return totalQuantity;
        }
        return 0;
    }

@Override
    public Integer findPromotionalPriceByProductId(Long productId) {
        return productRepository.findPromotionalPriceByProductId(productId);
    }


}