package com.example.demo.security.RestControllerAccount;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Employee;
import com.example.demo.security.Request.*;
import com.example.demo.security.jwt.JwtTokenUtil;
import com.example.demo.security.jwt_model.JwtRequest;
import com.example.demo.security.repository.SCAccountRepository;
import com.example.demo.security.service.*;
import com.example.demo.senderMail.Respone.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class JwtAuthenticationController {

    @Autowired
    private SCAccountRepository SCAccountRepository;

    @Autowired
    private SCAccountService accountService;

    @Autowired
    private SCEmployeeService SCEmployeeService;

    @Autowired
    private SCCustomerService SCCustomerService;

    @Autowired
    private SCUserService SCUserService;

    @Autowired
    private SCRefreshTokenService SCRefreshTokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        TokenResponse tokenResponse =  SCUserService.login(authenticationRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/RFToken/{refreshToken}")
    public ResponseEntity<TokenResponse> refreshToken(@PathVariable("refreshToken") String refreshToken) {
        System.out.println("Refresh Token");
        if (refreshToken != null) {
            Account accountEntity = SCRefreshTokenService.verifyExpiration(refreshToken);
            if (accountEntity != null) {
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setAccessToken(jwtTokenUtil.refreshToken(accountEntity));
                tokenResponse.setRefreshToken(refreshToken);
                System.out.println(tokenResponse);
                return ResponseEntity.ok(tokenResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/auth/register")
    public ResponseObject register(@RequestBody UserRequestDTO user) {
        return SCUserService.register(user);
    }

    @PostMapping("/auth/reSendOTP/{email}")
    public ResponseObject reSendOTP(@PathVariable String email) {
        return SCUserService.reSendOTP(email);
    }

    @PostMapping("/auth/confirm-otp")
    public boolean confirmOTP(@RequestBody OTPConfirmationDTO otpConfirmationDTO) {
        String email = otpConfirmationDTO.getEmail();
        String enteredOTP = otpConfirmationDTO.getEnteredOTP();
        return SCUserService.confirmOTP(email, enteredOTP);
    }

    @PostMapping("/auth/reset-password")
        public boolean resetPassword(@RequestBody OTPresetPassDTO otPresetPassDTO) {
        String email = otPresetPassDTO.getEmail();
        String pass = otPresetPassDTO.getNewPassword();
        return SCUserService.resetPassword(email, pass);
    }

    @PostMapping("/auth/forgot-password/{email}")
    public ResponseObject forgotPassword(@PathVariable String email) {
        return SCUserService.forgotPassword(email);
    }

    @PostMapping("/check-email")
    public ResponseEntity<Object> checkEmailExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = SCAccountRepository.existsByEmail(checkRequest.getEmail());
        return ResponseEntity.ok(exists);
    }


    @PostMapping("/check-account")
    public ResponseEntity<Object> checkAccountExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = SCAccountRepository.existsByAccount(checkRequest.getAccount());
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/check-phone-number")
    public ResponseEntity<Object> checkPhoneNumberExists(@RequestBody CheckRequest checkRequest) {
        boolean exists = SCAccountRepository.existsByPhoneNumber(checkRequest.getPhoneNumber());
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/api/ol/user")
    public ResponseEntity<?> getUserOl(@RequestParam(name = "username") String currentUsername) {
        Optional<Account> account = accountService.findByAccount(currentUsername);
        Map<String, Object> responseData = new HashMap<>();
        return account.map(acc -> {
            Optional<Customer> customerEntity = Optional.ofNullable(SCCustomerService.findByAccount_Id(acc.getId()));
            Optional<Employee> employeeEntity = Optional.ofNullable(SCEmployeeService.findByAccount_Id(acc.getId()));

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
