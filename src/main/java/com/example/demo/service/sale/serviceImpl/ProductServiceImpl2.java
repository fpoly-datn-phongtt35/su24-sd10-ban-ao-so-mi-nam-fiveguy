package com.example.demo.service.sale.serviceImpl;

import com.example.demo.entity.Product;
import com.example.demo.repository.sale.ProductRepository2;
import com.example.demo.service.sale.ProductService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl2 implements ProductService2 {

    @Autowired
    private ProductRepository2 productRepository2;

    @Override
    public List<Product> getProductsWithoutSaleOrExpiredPromotion() {
        Date currentDate = new Date();
        List<Product> productsWithoutSale = productRepository2.findByProductSalesIsNull();
        List<Product> productsWithExpiredPromotion = productRepository2.findByProductSalesEndDateBefore(currentDate);
        List<Product> mergedProducts = new ArrayList<>(productsWithoutSale);
        mergedProducts.addAll(productsWithExpiredPromotion);
        return mergedProducts;
    }

    @Override
    public Page<Product> filterProducts(Long categoryId, Long collarId, Long wristId, Long colorId, Long sizeId, Long materialId, String searchTerm, int page, int size) {
        List<Product> products = getProductsWithoutSaleOrExpiredPromotion();

        List<Product> filteredProducts = products.stream()
                .filter(product -> (categoryId == null || product.getCategory().getId().equals(categoryId)) &&
                        (collarId == null || product.getCollar().getId().equals(collarId)) &&
                        (wristId == null || product.getWrist().getId().equals(wristId)) &&
                        (colorId == null || product.getProductDetails().stream().anyMatch(pd -> pd.getColor().getId().equals(colorId))) &&
                        (sizeId == null || product.getProductDetails().stream().anyMatch(pd -> pd.getSize().getId().equals(sizeId))) &&
                        (materialId == null || product.getMaterial().getId().equals(materialId)) &&
                        (searchTerm == null || product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                product.getCode().toLowerCase().contains(searchTerm.toLowerCase())) &&
                        product.getStatus().equals(1)) // Assuming the status filter
                .collect(Collectors.toList());

        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + PageRequest.of(page, size).getPageSize()), filteredProducts.size());
        List<Product> output = filteredProducts.subList(start, end);

        return new PageImpl<>(output, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")), filteredProducts.size());
    }

}
