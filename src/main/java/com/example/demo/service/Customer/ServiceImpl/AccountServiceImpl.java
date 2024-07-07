package com.example.demo.service.Customer.ServiceImpl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Role;
import com.example.demo.repository.Customer.AccountRepositoryH;
import com.example.demo.service.Customer.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AccountServiceImpl  implements AccountService {
//    @Autowired
//    private AccountEmailSender accountEmailSender;

    @Autowired
    private AccountRepositoryH accountRepositoryH;

    @Override
    public List<Account> getAllAccount() {
        return accountRepositoryH.findAll();
    }

    @Override
    public List<Account> loadAccount() {
        return accountRepositoryH.loadAccount();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepositoryH.findById(id).orElse(null);
    }

    @Override
    public Optional<Account> findByAccount(String account) {
        Optional<Account> accountEntity = accountRepositoryH.findByAccount(account);
        if (accountEntity.isPresent()){
            return accountEntity;
        }
        return Optional.empty();
    }

    @Override
    public Page<Account> getAllAccountPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 1);
        return accountRepositoryH.findAll(pageable);
    }

    @Override
    public Account getByEmailAccount(String email){
        return (Account) accountRepositoryH.getByEmailAccount(email);
    }


    @Override
    public Account createAccount(Account accountEntity) {
        return accountRepositoryH.save(accountEntity);
    }

    @Override
    public Account save(Account accountEntity) {
        String email = accountEntity.getEmail();
        String account = accountEntity.getAccount();
        // Kiểm tra xem tên thương hiệu đã tồn tại trong cơ sở dữ liệu chưa
//        Optional<Account> existingEmail = accountRepository.findByEmail(email);
//        Optional<Account> existingAccount = accountRepository.findByAccount(account);
//        if (existingEmail != null) {
//            throw new IllegalArgumentException("Email đã tồn tại");
//        }
//        if (existingAccount != null) {
//            throw new IllegalArgumentException("Account đã tồn tại");
//        }
        return accountRepositoryH.save(accountEntity);
    }
//    @Override
//    public Account createAccount(Account accountEntity) {
//        Account account = accountRepository.save(accountEntity);
//        MailSender email = new MailSender();
//        email.setToEmail(new String[]{account.getEmail()});
//        email.setSubject("Chào mừng đến với trang Web trvelViVu");
//        email.setTitleEmail("Chúc mừng " + account.getAccount());
//        String confirmationLink = "http://localhost:3000/owner/comfirmmail?id=" + account.getId();
//        String emailBody = "Bạn đã đăng ký thành công. Vui lòng xác nhận email bằng cách nhấp vào liên kết sau: " + confirmationLink;
//        email.setBody(emailBody);
//        mailSenderService.sendEmail(email.getToEmail(), email.getSubject(), email.getTitleEmail(), emailBody);
//        return account;
//    }


    @Override
    public Account updateAccount(Account accountEntity, Long id) {
        Optional<Account> existingAddress = accountRepositoryH.findById(id);
        if (existingAddress.isPresent()) {
            Account account = existingAddress.get();
            account.setAccount(accountEntity.getAccount());
            account.setPassword(accountEntity.getPassword());
            account.setEmail(accountEntity.getEmail());
            account.setPhoneNumber(accountEntity.getPhoneNumber());
            account.setRole(accountEntity.getRole());
            account.setStatus(accountEntity.getStatus());

            return accountRepositoryH.save(account); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Accout với ID " + id);
//            return null;
        }
    }

    @Override
    public void deleteAccount(Long id) {
        // Kiểm tra xem khách hàng có tồn tại trước khi xóa
        if (accountRepositoryH.existsById(id)) {
            accountRepositoryH.deleteById(id);
        } else {
            // Xử lý lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Địa chỉ với ID " + id);
        }
    }

    @Override
    public List<Account> getAll() {
        return accountRepositoryH.findAll();
    }

    @Override
    public List<Account> getSStatus(Integer status) {
        List<Account> a = accountRepositoryH.getSStatus(status);
        return a;
    }


//    public  UserRequestDTO mapAccountToUserRequestDTO(Account account) {
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//        userRequestDTO.setAccount(account.getAccount());
//        userRequestDTO.setEmail(account.getEmail());
//        userRequestDTO.setId(account.getId());
//        // Các trường thông tin khác
//        return userRequestDTO;
//    }

    @Override
    public Optional<Account> findByAccount2(String username) {
        Optional<Account> account = accountRepositoryH.findByAccount(username);
        if (account.isPresent()){
            return account;
        }
        return Optional.empty();
    }

//    @Override
//    public List<UserRequestDTO> getAllAccount2() {
//        List<Account> accounts = accountRepository.findAll();
//        List<UserRequestDTO> userRequestDTOs = new ArrayList<>();
//        for (Account account : accounts) {
//            userRequestDTOs.add(mapAccountToUserRequestDTO(account));
//        }
//        return userRequestDTOs;
//    }

    @Override
    public Account createAccount2(Account accountEntity) {
        return null;
    }

    @Override
    public List<Account> findByEmail(String email) {

        List<Account> account = accountRepositoryH.findByEmail(email);
        if (account != null){
            return account;
        }
        return null;
    }


//OL

//    @Autowired
//    private AccountRepository accountRepository;

//    @Autowired
//    private CustomerService olCustomerService;

//    @Autowired
//    private EmployeeService olEmployeeService;

//    public  UserRequestDTO mapAccountToUserRequestDTO(AccountEntity account) {
//        UserRequestDTO userRequestDTO = new UserRequestDTO();
//        userRequestDTO.setAccount(account.getAccount());
//        userRequestDTO.setEmail(account.getEmail());
//        userRequestDTO.setId(account.getId());
//        // Các trường thông tin khác
//        return userRequestDTO;
//    }

    @Override
    public Optional<Account> findByAccountLogin(String username) {
        Optional<Account> account = accountRepositoryH.findByAccount(username);
        if (account.isPresent()) {
            return account;
        }
        return Optional.empty();
    }

//    @Override
//    public boolean updateUser(UserInfoRequest userInfoRequest) {
//        if (userInfoRequest.getAccount().getRole().getFullName().equals("CUSTOMER")) {
//            Optional<CustomerEntity> customer = Optional.ofNullable(olCustomerService.findByAccount_Id(userInfoRequest.getAccount().getId()));
//            CustomerEntity customerNew = customer.get();
//            customerNew.setFullName(userInfoRequest.getFullName());
//            customerNew.getAccount().setEmail(userInfoRequest.getAccount().getEmail());
//            customerNew.setId(userInfoRequest.getId());
//            customerNew.setGender(userInfoRequest.getGender());
//            customerNew.getAccount().setPhoneNumber(userInfoRequest.getAccount().getPhoneNumber());
//            customerNew.getAccount().setId(userInfoRequest.getAccount().getId());
//            customerNew.setAvatar(userInfoRequest.getAvatar());
//
//            accountRepository.save(customerNew.getAccount());
//            olCustomerService.save(customerNew);
//            return true;
//        } else if (userInfoRequest.getAccount().getRole().getFullName().equals("STAFF") || userInfoRequest.getAccount().getRole().equals("ADMIN")) {
//            Optional<Employees> employee = Optional.ofNullable(olEmployeeService.findByAccount_Id(userInfoRequest.getAccount().getId()));
//            Employees customerNew = employee.get();
//            customerNew.setFullName(userInfoRequest.getFullName());
//            customerNew.getAccount().setEmail(userInfoRequest.getAccount().getEmail());
//            customerNew.setId(userInfoRequest.getId());
//            customerNew.setGender(userInfoRequest.getGender());
//            customerNew.getAccount().setPhoneNumber(userInfoRequest.getAccount().getPhoneNumber());
//            customerNew.getAccount().setId(userInfoRequest.getAccount().getId());
//            customerNew.setAvatar(userInfoRequest.getAvatar());
//            accountRepository.save(customerNew.getAccount());
//            olEmployeeService.save(customerNew);
//            return true;
//        }
//        return false;
//    }

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
// END OL
    // Nhánh Tịnh
    @Override
    public boolean checkAccountExists(String account) {
        return accountRepositoryH.existsByAccount(account);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return accountRepositoryH.existsByEmail(email);
    }

    @Override
    public boolean checkPhoneNumberExists(String phoneNumber) {
        return accountRepositoryH.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Account saveAccountEmployee(Account accountEntity) {
        if (checkEmailExists(accountEntity.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
            Account account2 = new Account();
            Role role = new Role();
            role.setId(2L);
            account2.setAccount(accountEntity.getAccount());
            account2.setPassword("123456789");
            account2.setEmail(accountEntity.getEmail());
            account2.setPhoneNumber(accountEntity.getPhoneNumber());
            account2.setRole(role);
            account2.setStatus(1);

            return accountRepositoryH.save(account2);

    }

    @Override
    public Account saveAccountCustomer(Account accountEntity) {
        if (checkEmailExists(accountEntity.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Account account2 = new Account();
        Role role = new Role();
        role.setId(1L);
        account2.setAccount(accountEntity.getAccount());
        account2.setPassword("123456789");
        account2.setEmail(accountEntity.getEmail());
        account2.setPhoneNumber(accountEntity.getPhoneNumber());
        account2.setRole(role);
        account2.setStatus(1);

        return accountRepositoryH.save(account2);

    }

    @Override
    public Account updateAccountEmail(Account accountEntity, String email) {
        Optional<Account> existingAddress = Optional.ofNullable(accountRepositoryH.getByEmailAccount(email));
        if (existingAddress.isPresent()) {
            Account account = existingAddress.get();
            account.setAccount(accountEntity.getAccount());
//            account.setPassword(accountEntity.getPassword());
            account.setEmail(accountEntity.getEmail());
            account.setPhoneNumber(accountEntity.getPhoneNumber());
            account.setRole(accountEntity.getRole());
            account.setStatus(1);

            return accountRepositoryH.save(account); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Accout với ID " + email);
//            return null;
        }
    }


}
