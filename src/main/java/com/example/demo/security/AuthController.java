//package com.example.demo.security;
//
//import com.example.demo.entity.Account;
//import com.example.demo.entity.Employee;
//import com.example.demo.security.repository.EmployeeRepository;
//import com.example.demo.security.service.AccountServiceH;
//import com.example.demo.security.service.CustomerServiceH;
//import com.example.demo.security.service.EmployeeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Optional;
//
//
//@CrossOrigin("*")
//@Controller
//public class AuthController {
//
//    @Autowired
//    private AccountServiceH accountService;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//
//
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//
//    @ResponseBody
//    @GetMapping("/api/user")
//    public ResponseEntity<?> getEmployess() {
//
//        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
//            String currentUsername = authentication.getName();
//            Optional<Account> account = accountService.findByAccount(currentUsername);
//
//            if (account.isPresent()) {
//                Optional<Employee> employees = Optional.ofNullable(employeeRepository.findByAccount_Id(account.get().getId()));
//                return ResponseEntity.ok(employees);
//            }
//        }
//
//        return ResponseEntity.status(400).body(null);
//    }
//
////    @ResponseBody
////    @GetMapping("/api/ol/user")
////    public ResponseEntity<?> getUserOl(@RequestParam(name = "username") String currentUsername) {
////
////        Optional<AccountEntity> account = olAccountService.findByAccount(currentUsername);
////
////        if (account.isPresent()) {
////            Optional<CustomerEntity> customerEntity = Optional.ofNullable(olCustomerService.findByAccount_Id(account.get().getId()));
////            Optional<Employees> employeeEntity = Optional.ofNullable(olEmployeeService.findByAccount_Id(account.get().getId()));
////
////            if (customerEntity.isPresent()) {
////                return ResponseEntity.ok(customerEntity);
////            } else if (employeeEntity.isPresent()) {
////                // Trả về một đối tượng JSON với thuộc tính 'employeeLoggedIn' có giá trị true
////                Map<String, Object> responseData = new HashMap<>();
////                responseData.put("employeeLoggedIn", true);
////                return ResponseEntity.ok(responseData);
////            }
////        }
////        Map<String, Object> responseData = new HashMap<>();
////        responseData.put("loggedIn", false);
////        return ResponseEntity.ok(responseData);
////    }
//
//
//
////    @ResponseBody
////    @GetMapping("/api/ol/user/authenticated")
////    public boolean isAuthenticated() {
////
////        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
////
////    }
//
//    @GetMapping("/auth/login/form")
//    public String form(Model model){
//
//        return "login/index";
//
//    }
//
//    @GetMapping("/auth/login/success")
//    public String success(Model model) {
//        model.addAttribute("message", "Đăng nhập thành công");
//        return "forward:/auth/login/form";
//    }
//
//
//    @GetMapping("/auth/login/error")
//    public String error(Model model){
//        model.addAttribute("message","Sai thong tin dang nhap");
//        return "forward:/auth/login/form";
//    }
//
//    @GetMapping("/auth/logoff/success")
//    public String logoff(Model model){
//
//        model.addAttribute("message","Dang xuat thanh cong bấm login mới có thể đăng nhập tiếp");
//        return "forward:/auth/login/form";
//    }
//
//    @GetMapping("/auth/access/denied")
//    public String denied(Model model){
//        model.addAttribute("message","Ban ko co quyen truy xuat");
//        return "login/index";
//    }
//
//}