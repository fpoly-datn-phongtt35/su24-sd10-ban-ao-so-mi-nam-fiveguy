package com.example.demo.security.RestControllerAccount;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.Request.*;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.jwt_model.JwtRequest;
import com.example.demo.security.repository.AccountRepository;
import com.example.demo.security.service.*;
import com.example.demo.senderMail.Respone.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class JwtAuthenticationController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        TokenResponse tokenResponse =  userService.login(authenticationRequest);

        System.out.println(tokenResponse.getAccessToken());
        System.out.println(tokenResponse.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/RFToken/{refreshToken}")
    public ResponseEntity<TokenResponse> refreshToken(@PathVariable("refreshToken") String refreshToken) {
        if (refreshToken != null) {
            System.out.println("Refresh token success");
            Account accountEntity = refreshTokenService.verifyExpiration(refreshToken);
            if (accountEntity != null) {
//                String newAccessToken = jwtTokenUtil.refreshToken(existingRefreshToken.getAccountEntity());
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setAccessToken(jwtTokenUtil.refreshToken(accountEntity));
                tokenResponse.setRefreshToken(refreshToken);
                System.out.println(tokenResponse);
                return ResponseEntity.ok(tokenResponse);
            } else {
                throw new RuntimeException("Invalid refresh token");
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/auth/register")
    public ResponseObject register(@RequestBody UserRequestDTO user) {
        return userService.register(user);
    }

    @PostMapping("/auth/reSendOTP/{email}")
    public ResponseObject reSendOTP(@PathVariable String email) {
//        System.out.println(email);
        return userService.reSendOTP(email);
    }

    @PostMapping("/auth/confirm-otp")
    public boolean confirmOTP(@RequestBody OTPConfirmationDTO otpConfirmationDTO) {
        String email = otpConfirmationDTO.getEmail();
        String enteredOTP = otpConfirmationDTO.getEnteredOTP();
        return userService.confirmOTP(email, enteredOTP);
    }

    @PostMapping("/auth/reset-password")
        public boolean resetPassword(@RequestBody OTPresetPassDTO otPresetPassDTO) {
        String email = otPresetPassDTO.getEmail();
        String pass = otPresetPassDTO.getNewPassword();
        return userService.resetPassword(email, pass);
    }

    @PostMapping("/auth/forgot-password/{email}")
    public ResponseObject forgotPassword(@PathVariable String email) {
        return userService.forgotPassword(email);
    }

    @PostMapping("/check-email")
    public ResponseEntity<Object> checkEmailExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = accountRepository.existsByEmail(checkRequest.getEmail());
        return ResponseEntity.ok(exists);
    }


    @PostMapping("/check-account")
    public ResponseEntity<Object> checkAccountExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = accountRepository.existsByAccount(checkRequest.getAccount());
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/check-phone-number")
    public ResponseEntity<Object> checkPhoneNumberExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = accountRepository.existsByPhoneNumber(checkRequest.getPhoneNumber());
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/api/ol/user")
    public ResponseEntity<?> getUserOl(@RequestParam(name = "username") String currentUsername) {

        Optional<Account> account = accountService.findByAccount(currentUsername);

        Map<String, Object> responseData = new HashMap<>();

        return account.map(acc -> {
            Optional<Customer> customerEntity = Optional.ofNullable(customerService.findByAccount_Id(acc.getId()));
            Optional<Employee> employeeEntity = Optional.ofNullable(employeeService.findByAccount_Id(acc.getId()));

            if (customerEntity.isPresent()) {
                return ResponseEntity.ok(customerEntity);
            } else if (employeeEntity.isPresent()) {
                return ResponseEntity.ok(employeeEntity);
            } else {
                responseData.put("loggedIn", false);
                return ResponseEntity.ok(responseData);
            }
        }).orElseGet(() -> {
            responseData.put("loggedIn", false);
            return ResponseEntity.ok(responseData);
        });
    }



}
