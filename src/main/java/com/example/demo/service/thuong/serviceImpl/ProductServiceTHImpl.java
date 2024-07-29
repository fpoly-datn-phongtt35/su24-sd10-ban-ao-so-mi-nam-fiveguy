package com.example.demo.service.thuong.serviceImpl;

import com.example.demo.advice.DuplicateException;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetail;
import com.example.demo.model.request.thuong.ProductRequestTH;
import com.example.demo.model.response.thuong.ProductResponse;
import com.example.demo.repository.thuong.ImageRepositoryTH;
import com.example.demo.repository.thuong.ProductDetailRepositoryTH;
import com.example.demo.repository.thuong.ProductRepositoryTH;
import com.example.demo.repository.thuong.ProductResponseRepositoryTH;
import com.example.demo.service.thuong.ProductServiceTH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceTHImpl implements ProductServiceTH {
    @Autowired
    private ProductRepositoryTH productRepository;

    @Autowired
    private ProductDetailRepositoryTH productDetailRepository;

    @Autowired
    private ImageRepositoryTH imageRepository;

    @Autowired
    private ProductResponseRepositoryTH productResponseRepository;

    @Override
    public Product create(ProductRequestTH productRequest) {
        Product exProductByName = productRepository.findByName(productRequest.getName());
        if (exProductByName != null) {
            throw new DuplicateException("Trùng tên sản phẩm", "name");
        }
        Product exProductByCode = productRepository.findByCode(productRequest.getCode());
        if (exProductByCode != null) {
            throw new DuplicateException("Trùng mã sản phẩm", "code");
        }
        Product product = new Product();
        product.setCode(productRequest.getCode());
        product.setName(productRequest.getName());
        product.setImportPrice(productRequest.getImportPrice());
        product.setPrice(productRequest.getPrice());
        product.setDescribe(productRequest.getDescribe());
        product.setCreatedAt(new Date());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCategory(productRequest.getCategory());
        product.setSupplier(productRequest.getSupplier());
        product.setMaterial(productRequest.getMaterial());
        product.setWrist(productRequest.getWrist());
        product.setCollar(productRequest.getCollar());
        product.setStatus(productRequest.getStatus());
        Product saveProduct = productRepository.save(product);
        if (productRequest.getProductDetails() != null && productRequest.getImages() != null) {
            List<ProductDetail> productDetails = new ArrayList<>();
            for (ProductDetail dt : productRequest.getProductDetails()) {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setProduct(product);
                productDetail.setSize(dt.getSize());
                productDetail.setColor(dt.getColor());
                if(dt.getQuantity() == 0) {
                    productDetail.setStatus(0);
                } else {
                    productDetail.setStatus(dt.getStatus());
                }
                productDetail.setQuantity(dt.getQuantity());
                productDetail.setCreatedAt(new Date());
                productDetail.setBarcode(dt.getBarcode());

                productDetails.add(productDetail);
            }

            productDetailRepository.saveAll(productDetails);
            List<Image> images = new ArrayList<>();
            for (Image img : productRequest.getImages()) {
                Image image = new Image();
                image.setName(img.getName());
                image.setPath(img.getPath());
                image.setCreatedAt(new Date());
                image.setStatus(1);
                image.setProduct(saveProduct);
                image.setColor(img.getColor());
                images.add(image);
            }
            imageRepository.saveAll(images);
        }

        return saveProduct;
    }

    @Override
    public Product update(ProductRequestTH productRequestTH, Long id) {
        Product exProductByName = productRepository.findByName(productRequestTH.getName());
        if (exProductByName != null && !exProductByName.getId().equals(id)) {
            throw new DuplicateException("Trùng tên sản phẩm", "name");
        }
        Product exProductByCode = productRepository.findByCode(productRequestTH.getCode());
        if (exProductByCode != null && !exProductByCode.getId().equals(id)) {
            throw new DuplicateException("Trùng mã sản phẩm", "code");
        }
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setCode(productRequestTH.getCode());
            product.setName(productRequestTH.getName());
            product.setImportPrice(productRequestTH.getImportPrice());
            product.setPrice(productRequestTH.getPrice());
            product.setDescribe(productRequestTH.getDescribe());
            product.setUpdatedAt(new Date());
            product.setCategory(productRequestTH.getCategory());
            product.setSupplier(productRequestTH.getSupplier());
            product.setMaterial(productRequestTH.getMaterial());
            product.setWrist(productRequestTH.getWrist());
            product.setCollar(productRequestTH.getCollar());
            product.setStatus(productRequestTH.getStatus());
            if (productRequestTH.getProductDetails() != null) {
                product.getProductDetails().clear();
                productRequestTH.getProductDetails().forEach(dt -> {
                    if(dt.getQuantity() == 0) {
                        dt.setStatus(0);
                    } else {
                        dt.setStatus(dt.getStatus());
                    }
                    dt.setProduct(product);
                    product.getProductDetails().add(dt);
                });
            }
            if (productRequestTH.getImages() != null) {
                product.getImages().clear();
                productRequestTH.getImages().forEach(img -> {
                    img.setProduct(product);
                    img.setUpdatedAt(new Date());
                    product.getImages().add(img);
                });
            }
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Product updateStatus(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setUpdatedAt(new Date());
            product.setStatus(1);
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Product delete(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setStatus(0);
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public Page<ProductResponse> getProducts(int page, int size, String keyword, String sortField, String sortDirection, BigDecimal minPrice,BigDecimal maxPrice, Integer status) {
        Sort sort = Sort.by(sortField);
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        if (keyword == null || keyword.isEmpty())
            return productResponseRepository.findAllAndStatus( minPrice, maxPrice, status,pageable);
        else return productResponseRepository.findByNameOrCode(minPrice, maxPrice, keyword, status,pageable);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}
