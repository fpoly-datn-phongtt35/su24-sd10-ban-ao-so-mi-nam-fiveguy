package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Employee;
import com.example.demo.entity.ProductDetail;
import com.example.demo.repository.tinh.ProductDetailSpecificationTinh;
import com.example.demo.repository.tinh.ProductDetaillRepositoryTinh;
import com.example.demo.service.tinh.ProductDetailServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailServiceImplTinh implements ProductDetailServiceTinh {
    @Autowired
    ProductDetaillRepositoryTinh productRepositoryTinh;

    @Autowired
    ProductDetaillRepositoryTinh productDetaillRepositoryTinh;

    @Override
    public List<ProductDetail> getAll(){
        return productRepositoryTinh.findAll();
    }

    @Override
    public Page<ProductDetail> findProductDetal(String name, String code, BigDecimal price, Pageable pageable) {

        Specification<ProductDetail> spec = Specification.where(ProductDetailSpecificationTinh.hasCode(code))
                .and(ProductDetailSpecificationTinh.hasName(name))
                .and(ProductDetailSpecificationTinh.hasPrice(price));


        return productDetaillRepositoryTinh.findAll(spec, pageable);
    }

    @Override
    public ProductDetail update(Long id, ProductDetail employees){
        Optional<ProductDetail> existingEmployee = productDetaillRepositoryTinh.findById(id);
        if (existingEmployee.isPresent()) {
            ProductDetail employees1 = existingEmployee.get();
            employees1.setQuantity(employees.getQuantity());
            employees1.setBarcode(employees.getBarcode());
            employees1.setCreatedAt(employees.getCreatedAt());
            employees1.setUpdatedAt(employees.getUpdatedAt());
            employees1.setCreatedBy(employees.getCreatedBy());
            employees1.setUpdatedBy(employees.getUpdatedBy());
            employees1.setProduct(employees.getProduct());
            employees1.setSize(employees.getSize());
            employees1.setColor(employees.getColor());
            employees1.setStatus(employees.getStatus());

            return productDetaillRepositoryTinh.save(employees1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
//            return null;
        }
    }
}
