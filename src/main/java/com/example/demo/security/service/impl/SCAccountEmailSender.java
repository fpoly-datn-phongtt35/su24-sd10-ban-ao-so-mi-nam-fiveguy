package com.example.demo.security.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class SCAccountEmailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public SCAccountEmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendAccountCreationEmail(String recipientEmail, String accountName, Long id, String confirmationCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(recipientEmail);
            mimeMessageHelper.setSubject("Chào mừng bạn đến với trang Web FiveGuys");
            mimeMessageHelper.setText(
                    "Xin Chào " + accountName + ",\n\n"
                            + "Cảm ơn bạn đã đăng ký trên nền tảng của chúng tôi! Tài khoản của bạn có ID: " + id + " đã được tạo.\n\n"
//                            + "Vui lòng xác nhận email của bạn bằng cách nhập mã sau vào chỗ trống được cung cấp: " + confirmationCode + ".\n\n"
//                            + "Trân trọng,\n Nhóm nền tảng của bạn" + ".\n\n"
//                            + "Link nhập mã:" + "http://localhost:8080/login/" + id
            );

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý khi gửi email thất bại ở đây
        }
    }
}
