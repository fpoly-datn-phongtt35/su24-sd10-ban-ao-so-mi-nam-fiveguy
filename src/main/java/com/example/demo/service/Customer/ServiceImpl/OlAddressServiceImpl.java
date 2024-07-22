package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Address;
import com.example.demo.entity.Customer;
import com.example.demo.repository.Customer.OLAddressRepository;
import com.example.demo.service.Customer.CustomerProfileServiceH;
import com.example.demo.service.Customer.OlAddressService;
import com.example.demo.service.Customer.OlCustomerService;
import com.example.demo.service.Customer.OlEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service

public class OlAddressServiceImpl implements OlAddressService {

    @Autowired
    private OLAddressRepository repository;

    @Autowired
    private CustomerProfileServiceH olAccountService;

    @Autowired
    private OlCustomerService olCustomerService;

    @Autowired
    private OlEmployeeService olEmployeeService;

    @Override
    public List<Address> getAddressListByUsername(String username) {
        Optional<Account> account = olAccountService.findByAccount(username);

        if (account.isPresent()) {
            // Lấy thông tin khách hàng từ tài khoản
            Optional<Customer> customerEntity = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
            if (customerEntity.isPresent()) {
                return repository.findAllByCustomer_IdAndStatus(customerEntity.get().getId(),1);
            }
        }
        return Collections.emptyList(); // Trả về danh sách trống nếu không tìm thấy thông tin khách hàng hoặc địa chỉ
    }

    @Override
    public void deleteAddress(Long id) {
        repository.deleteById(id);

    }

    @Override
    public boolean update(Address addressRequest) {
        if (addressRequest.getDefaultAddress() && addressRequest.getDefaultAddress() != null) {
            // Lấy danh sách địa chỉ của khách hàng
            List<Address> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
            for (Address address : customerAddresses) {
                if (!address.getId().equals(addressRequest.getId())) {
                    address.setDefaultAddress(false);
                    repository.save(address);
                }
            }
        }

        addressRequest.setUpdatedAt(new Date());
        repository.save(addressRequest);
        return true;
    }

    @Override
    public boolean addAddress(Address addressRequest) {
        if (addressRequest.getDefaultAddress() && addressRequest.getDefaultAddress() != null) {
            // Lấy danh sách địa chỉ của khách hàng
            List<Address> customerAddresses = repository.findAllByCustomer_IdAndStatus(addressRequest.getCustomer().getId(),1);
            for (Address address : customerAddresses) {
                if (!address.getId().equals(addressRequest.getId())) {
                    address.setDefaultAddress(false);
                    repository.save(address);
                }
            }
        }
        addressRequest.setCreatedAt(new Date());
        addressRequest.setStatus(1);
        addressRequest.setId(null);
        repository.save(addressRequest);
        return true;
    }

    @Override
    public Address findByDefaultAddressTrue(String username) {
        Optional<Account> account = olAccountService.findByAccount(username);
        if (account.isPresent()) {
            Optional<Customer> customerEntity = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
//            Optional<Employees> employeeEntity = Optional.ofNullable(olEmployeeService.findByAccount_Id(account.get().getId()));

            if (customerEntity.isPresent()) {
                List<Address> addressEntities = repository.findByCustomer_FullName(customerEntity.get().getFullName());
                for (Address addressEntity : addressEntities) {
                    if ((addressEntity.getDefaultAddress())) {
                        return addressEntity;
                    }
                }
            }
        }
        return null;
    }


    @Override
    public Optional<Address> findById(Long id) {
        Optional<Address> addressEntity = repository.findById(id);
        if (addressEntity.isPresent()){
            return addressEntity;
        }

        return Optional.empty();
    }


}
