package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Address;
import com.example.demo.repository.Customer.AddressRepositoryH;
import com.example.demo.service.Customer.AccountServiceH;
import com.example.demo.service.Customer.AddressServiceH;
import com.example.demo.service.Customer.CustomerServiceH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceHImpl implements AddressServiceH {

    private final AddressRepositoryH addressRepositoryH;

    @Autowired
    public AddressServiceHImpl(AddressRepositoryH addressRepositoryH) {
        this.addressRepositoryH = addressRepositoryH;
    }


    @Override
    public List<Address> getAllAddress() {
        return addressRepositoryH.findAll();
    }

    @Override
    public List<Address> findByCustomerId(Long customerId) {
        return addressRepositoryH.findByCustomerId(customerId);
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepositoryH.findById(id).orElse(null);
    }

    @Override
    public Page<Address> getAllAddressPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 1);
        return addressRepositoryH.findAll(pageable);
    }

    @Override
    public Address createAddress(Address addressEntity) {
        return addressRepositoryH.save(addressEntity);
    }

    @Override
    public Address updateAddress(Address addressEntity, Long id) {
        Optional<Address> existingAddress = addressRepositoryH.findById(id);
        if (existingAddress.isPresent()) {
            Address address = existingAddress.get();
            address.setName(addressEntity.getName());
            address.setPhoneNumber(addressEntity.getPhoneNumber());
            address.setAddress(addressEntity.getAddress());
            address.setAddressId(addressEntity.getAddressId());
            address.setAddressType(addressEntity.getAddressType());
            address.setCustomer(addressEntity.getCustomer());
            address.setCreatedAt(addressEntity.getCreatedAt());
            address.setUpdatedAt(addressEntity.getUpdatedAt());
            address.setStatus(addressEntity.getStatus());

            return addressRepositoryH.save(address); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
//            return null;
        }
    }

    @Override
    public void deleteAddress(Long id) {
        // Kiểm tra xem khách hàng có tồn tại trước khi xóa
        if (addressRepositoryH.existsById(id)) {
            addressRepositoryH.deleteById(id);
        } else {
            // Xử lý lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
        }
    }

    @Override
    public List<Address> getSStatus(Integer status) {
        List<Address> a = addressRepositoryH.getSStatus(status);
        return a;
    }

    @Override
    public List<Address> getAddressListByUsername(String username) {
        return null;
    }


//OL

    @Autowired
    private AddressRepositoryH repository;

    @Autowired
    private AccountServiceH olAccountServiceH;

    @Autowired
    private CustomerServiceH olCustomerServiceH;

//    @Autowired
//    private EmployeeService olEmployeeService;

//    @Override
//    public List<Address> getAddressListByUsername(String username) {
//        Optional<Account> account = olAccountServiceH.findByAccount(username);
//
//        if (account.isPresent()) {
//            // Lấy thông tin khách hàng từ tài khoản
//            Optional<Customer> customerEntity = Optional.ofNullable(olCustomerServiceH.findByAccount_Id(account.get().getId()));
//            if (customerEntity.isPresent()) {
//                return repository.findAllByCustomer_IdAndStatus(customerEntity.get().getId(),1);
//            }
//        }
//        return Collections.emptyList(); // Trả về danh sách trống nếu không tìm thấy thông tin khách hàng hoặc địa chỉ
//    }

//    @Override
//    public void deleteAddress(Long id) {
//        repository.deleteById(id);
//
//    }

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
        return null;
    }

//    @Override
//    public Address findByDefaultAddressTrue(String username) {
//        Optional<Account> account = olAccountServiceH.findByAccount(username);
//        if (account.isPresent()) {
//            Optional<Customer> customerEntity = Optional.ofNullable(olCustomerServiceH.findByAccount_Id(account.get().getId()));
////            Optional<Employees> employeeEntity = Optional.ofNullable(olEmployeeService.findByAccount_Id(account.get().getId()));
//
//            if (customerEntity.isPresent()) {
//                List<Address> addressEntities = repository.findByCustomer_FullName(customerEntity.get().getFullName());
//                for (Address addressEntity : addressEntities) {
//                    if ((addressEntity.getDefaultAddress())) {
//                        return addressEntity;
//                    }
//                }
//            }
//        }
//        return null;
//    }


    @Override
    public Optional<Address> findById(Long id) {
        Optional<Address> addressEntity = repository.findById(id);
        if (addressEntity.isPresent()){
            return addressEntity;
        }

        return Optional.empty();
    }

// END OL
}
