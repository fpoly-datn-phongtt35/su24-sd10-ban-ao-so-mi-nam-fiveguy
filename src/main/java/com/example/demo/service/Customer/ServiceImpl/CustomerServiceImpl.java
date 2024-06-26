package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.repository.Customer.CustomerRepositoryH;
import com.example.demo.service.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepositoryH customerRepositoryH;

    // get all Customer
    @Override
    public List<Customer> getAll(){
        return customerRepositoryH.findAll();
    }

    //get all Employ status = 1 (dang làm)
    @Override
    public List<Customer> getAllStatusDangLam(){
        return customerRepositoryH.getAllStatusDangLam();
    }

    @Override
    public Customer getById(Long id){
        return customerRepositoryH.findById(id).orElse(null);
    }

    @Override
    public Page<Customer> phanTrang(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        return customerRepositoryH.findAll(pageable);
    }

    @Override
    public List<Customer> getAllStatus(Integer status){
        return customerRepositoryH.getAllStatus(status);
    }

    @Override
    public Customer create(Customer customers){
        Customer customers1 = new Customer();
        String randomCode = generateRandomCode(6);
        customers1.setCode(randomCode);
        customers1.setFullName(customers.getFullName());
        customers1.setAvatar(customers.getAvatar());
        customers1.setBirthDate(customers.getBirthDate());
        customers1.setGender(customers.getGender());
        customers1.setAccount(customers.getAccount());
        customers1.setCreatedAt(new Date());
        customers1.setUpdatedAt(new Date());
        customers1.setCreatedBy("admin");
        customers1.setUpdatedBy("admin");
        customers1.setCustomerType(customers.getCustomerType());
        customers1.setStatus(1);

        return customerRepositoryH.save(customers1);

    }

    @Override
    public void delete(Long id){
        customerRepositoryH.deleteById(id);
    }

    @Override
    public  Customer update(Long id, Customer customers){
        Optional<Customer> Customer = customerRepositoryH.findById(id);
        if (Customer.isPresent()) {
            Customer customers1 = Customer.get();
            customers1.setCode(customers.getCode());
            customers1.setFullName(customers.getFullName());
            customers1.setAvatar(customers.getAvatar());
            customers1.setBirthDate(customers.getBirthDate());
            customers1.setGender(customers.getGender());
            customers1.setAccount(customers.getAccount());
            customers1.setCreatedAt(customers.getCreatedAt());
            customers1.setUpdatedAt(new Date());
            customers1.setCreatedBy("admin");
            customers1.setUpdatedBy("admin");
            customers1.setCustomerType(customers.getCustomerType());
            customers1.setStatus(customers.getStatus());

            return customerRepositoryH.save(customers1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
//            return null;
        }
    }

    private String generateRandomCode(int length) {
        String uppercaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(uppercaseCharacters.length());
            char randomChar = uppercaseCharacters.charAt(randomIndex);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }

    //delete theo status
    @Override
    public  Customer updateRole(Long id, Customer customers){
        Optional<Customer> existingCustomer = customerRepositoryH.findById(id);
        if (existingCustomer.isPresent()) {
            Customer customers1 = existingCustomer.get();
            customers1.setCode(customers.getCode());
            customers1.setFullName(customers.getFullName());
            customers1.setAvatar(customers.getAvatar());
            customers1.setBirthDate(customers.getBirthDate());
            customers1.setGender(customers.getGender());
            customers1.setAccount(customers.getAccount());
            customers1.setCreatedAt(customers.getCreatedAt());
            customers1.setUpdatedAt(new Date());
            customers1.setCreatedBy("admin");
            customers1.setUpdatedBy("admin");
            customers1.setCustomerType(customers.getCustomerType());
            customers1.setStatus(2);

            return customerRepositoryH.save(customers1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Customer với ID " + id);
//            return null;
        }
    }

    // search ma
    @Override
    public Page<Customer>  searchMa(String ma, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customersList = customerRepositoryH.searchMa(ma, pageable);
        return customersList;
    }
}
