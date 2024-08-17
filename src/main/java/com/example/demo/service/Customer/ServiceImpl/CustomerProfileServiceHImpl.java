package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.model.request.customer.UserInfoRequest;
import com.example.demo.repository.Customer.OlAccountRepository;
import com.example.demo.service.Customer.CustomerProfileServiceH;
import com.example.demo.service.Customer.OlCustomerService;
import com.example.demo.service.Customer.OlEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerProfileServiceHImpl implements CustomerProfileServiceH {

    @Autowired
    private OlAccountRepository accountRepository;

    @Autowired
    private OlCustomerService olCustomerService;

    @Autowired
    private OlEmployeeService olEmployeeService;

//    public  UserRequestDTO mapAccountToUserRequestDTO(AccountEntity account) {
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//        userRequestDTO.setAccount(account.getAccount());
//        userRequestDTO.setEmail(account.getEmail());
//        userRequestDTO.setId(account.getId());
//        // Các trường thông tin khác
//        return userRequestDTO;
//    }

    @Override
    public Optional<Account> findByAccount(String username) {
        Optional<Account> account = accountRepository.findByAccount(username);
        if (account.isPresent()) {
            return account;
        }
        return Optional.empty();
    }

    @Override
    public boolean updateUser(UserInfoRequest userInfoRequest) {
        if (userInfoRequest.getAccount().getRole().getFullName().equals("CUSTOMER")) {
            Optional<Customer> customer = Optional.ofNullable(olCustomerService.findByAccount_Id(userInfoRequest.getAccount().getId()));
            Customer customerNew = customer.get();
            customerNew.setFullName(userInfoRequest.getFullName());
            customerNew.getAccount().setEmail(userInfoRequest.getAccount().getEmail());
            customerNew.setId(userInfoRequest.getId());
            customerNew.setGender(userInfoRequest.getGender());
            customerNew.getAccount().setPhoneNumber(userInfoRequest.getAccount().getPhoneNumber());
            customerNew.getAccount().setId(userInfoRequest.getAccount().getId());
            customerNew.setAvatar(userInfoRequest.getAvatar());

            accountRepository.save(customerNew.getAccount());
            olCustomerService.save(customerNew);
            return true;
        } else if (userInfoRequest.getAccount().getRole().getFullName().equals("STAFF") || userInfoRequest.getAccount().getRole().equals("ADMIN")) {
            Optional<Employee> employee = Optional.ofNullable(olEmployeeService.findByAccount_Id(userInfoRequest.getAccount().getId()));
            Employee customerNew = employee.get();
            customerNew.setFullName(userInfoRequest.getFullName());
            customerNew.getAccount().setEmail(userInfoRequest.getAccount().getEmail());
            customerNew.setId(userInfoRequest.getId());
            customerNew.setGender(userInfoRequest.getGender());
            customerNew.getAccount().setPhoneNumber(userInfoRequest.getAccount().getPhoneNumber());
            customerNew.getAccount().setId(userInfoRequest.getAccount().getId());
            customerNew.setAvatar(userInfoRequest.getAvatar());
            accountRepository.save(customerNew.getAccount());
            olEmployeeService.save(customerNew);
            return true;
        }
        return false;
    }
//
//    @Override
//    public List<UserRequestDTO> getAllAccount() {
//        List<AccountEntity> accounts = accountRepository.findAll();
//        List<UserRequestDTO> userRequestDTOs = new ArrayList<>();
//        for (AccountEntity account : accounts) {
//            userRequestDTOs.add(mapAccountToUserRequestDTO(account));
//        }
//        return userRequestDTOs;
//    }
//
//    @Override
//    public AccountEntity createAccount(AccountEntity accountEntity) {
//        return accountRepository.save(accountEntity);
//    }

}
