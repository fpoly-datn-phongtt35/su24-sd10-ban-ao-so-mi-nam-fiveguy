package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Customer;
import com.example.demo.repository.Customer.OlCustomerRepository;
import com.example.demo.service.Customer.OlCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OlCustomerServiceImpl implements OlCustomerService {

    @Autowired
    private OlCustomerRepository olCustomerRepository;

    @Override
    public Optional<Customer> findById(Long Id) {
        Optional<Customer> customer = olCustomerRepository.findById(Id);

        if (customer.isPresent()){
            return customer;
        }

        return Optional.empty();
    }

    @Override
    public Customer findByAccount_Id(Long accountId) {
        return olCustomerRepository.findByAccount_Id(accountId);
    }

    @Override
    public Customer save(Customer customerEntity) {
        return olCustomerRepository.save(customerEntity);
    }
}
