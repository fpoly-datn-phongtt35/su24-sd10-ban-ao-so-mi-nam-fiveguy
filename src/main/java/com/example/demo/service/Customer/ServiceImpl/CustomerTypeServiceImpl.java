package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.entity.CustomerType;
import com.example.demo.repository.Customer.CustomerTypeRepository;
import com.example.demo.service.Customer.CustomerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CustomerTypeServiceImpl implements CustomerTypeService {
    @Autowired
    CustomerTypeRepository customerTypeRepository;

    @Override
    public List<CustomerType> getAll() {
        return customerTypeRepository.findAll();
    }

    @Override
    public Page<CustomerType> phanTrang(Integer pageNum, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNum, pageNo);
        return customerTypeRepository.findAll(pageable);
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
        customerTypeRepository.deleteById(id);
    }

    @Override
    public CustomerType update(Long id, CustomerType customerTypes) {
        Optional<CustomerType> CustomerType = customerTypeRepository.findById(id);
        if (CustomerType.isPresent()) {
            CustomerType customerType1 = CustomerType.get();
            customerType1.setName(customerTypes.getName());
            customerType1.setCreatedAt(customerTypes.getCreatedAt());
            customerType1.setUpdatedAt(new Date());
            customerType1.setStatus(customerTypes.getStatus());

            return customerTypeRepository.save(customerType1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
//            return null;
        }
    }
}
