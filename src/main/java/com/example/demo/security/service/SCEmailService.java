package com.example.demo.security.service;

public interface SCEmailService {

    void sendHtmlEmail(String toEmail, String subject, String htmlContent);
}
