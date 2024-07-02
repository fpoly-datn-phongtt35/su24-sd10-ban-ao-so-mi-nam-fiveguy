package com.example.demo.senderMail.restController;//package com.example.demo.senderMail.restController;
//
//import com.example.demo.entity.AccountEntity;
//import com.example.demo.senderMail.Respone.ConfirmationRequest;
//import com.example.demo.senderMail.Respone.ResponseObject;
//import com.example.demo.senderMail.UserService;
//import com.example.demo.service.serviceiplm.AccountServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("")
//public class OTPConfirmationController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AccountServiceImpl accountService;
//
//    @PostMapping("/confirm-otp")
//    public ResponseEntity<ResponseObject> confirmOTP(@RequestBody ConfirmationRequest request) {
//        String enteredOTP = request.getOtp();
//        String userEmail = request.getEmail();
//        // Gọi service để xác nhận OTP
//        ResponseObject response = userService.confirmOTP(userEmail, enteredOTP);
//        if ("200".equals(response.getStatus())) {
//            return ResponseEntity.ok().body(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PostMapping("/verify-otp")
//    public ResponseEntity<ResponseObject> verifyOTP(@RequestBody ConfirmationRequest confirmationRequest) {
//        // Lấy thông tin người dùng từ email
//        Optional<AccountEntity> user = accountService.findByAccount(confirmationRequest.getEmail());
//        if (user.isPresent()) {
//            AccountEntity userEntity = user.get();
//            String enteredOTP = confirmationRequest.getOtp();
//            String storedOTP = userEntity.getConfirmationCode();
//            if (enteredOTP.equals(storedOTP)) {
//                // Mã OTP khớp, cập nhật trạng thái xác nhận trong cơ sở dữ liệu
//                userEntity.setActive(true);
//                accountService.save(userEntity); // Lưu thông tin cập nhật vào cơ sở dữ liệudata is not defined
//                return new ResponseEntity<>(new ResponseObject("200", "Đã xác minh OTP và kích hoạt tài khoản.", null), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(new ResponseObject("400", "OPT không hợp lệ.", null), HttpStatus.BAD_REQUEST);
//            }
//        } else {
//            return new ResponseEntity<>(new ResponseObject("404", "Không tìm thấy người dùng.", null), HttpStatus.NOT_FOUND);
//        }
//    }
//}
