package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.CustomerType;
import com.example.demo.repository.Customer.CustomerTypeRepositoryH;
import com.example.demo.service.Customer.CustomerTypeServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerTypeServiceHImpl implements CustomerTypeServiceH {
    @Autowired
    CustomerTypeRepositoryH customerTypeRepositoryH;

    @Override
    public List<CustomerType> getAll() {
        return customerTypeRepositoryH.findAll();
    }

    @Override
    public CustomerType getById(Long id) {
        return customerTypeRepositoryH.findById(id).orElse(null);
    }

    @Override
    public Page<CustomerType> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return customerTypeRepositoryH.findAll(pageable);
    }

    @Override
    public CustomerType create(CustomerType customerTypes) {
        CustomerType customerTypes1 = new CustomerType();
        customerTypes1.setName(customerTypes.getName());
        customerTypes1.setCreatedAt(new Date());
        customerTypes1.setUpdatedAt(new Date());
        customerTypes1.setStatus(1);
        return null;
    }

    @Override
    public void delete(Long id) {
        customerTypeRepositoryH.deleteById(id);
    }

    @Override
    public CustomerType update(Long id, CustomerType customerTypes) {
        Optional<CustomerType> CustomerType = customerTypeRepositoryH.findById(id);
        if (CustomerType.isPresent()) {
            CustomerType customerType1 = CustomerType.get();
            customerType1.setName(customerTypes.getName());
            customerType1.setCreatedAt(customerTypes.getCreatedAt());
            customerType1.setUpdatedAt(new Date());
            customerType1.setStatus(customerTypes.getStatus());

            return customerTypeRepositoryH.save(customerType1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
//            return null;
        }
    }
}
