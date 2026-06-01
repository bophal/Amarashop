package com.bookshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String username, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome - Your Account Has Been Created");
        message.setText(
            "Hello " + username + ",\n\n" +
            "Your account has been successfully created.\n\n" +
            "Your temporary password is: " + tempPassword + "\n\n" +
            "Please log in and change your password immediately.\n\n" +
            "Note: Your email address is private and will only be used\n" +
            "for password resets and important notifications.\n\n" +
            "Regards,\nThe BookShop Team"
        );
        mailSender.send(message);
    }
    public void sendPasswordResetEmail(String toEmail, String username, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your New Password");
        message.setText(
            "Hello " + username + ",\n\n" +
            "Your password has been reset as requested.\n\n" +
            "Your new temporary password is: " + newPassword + "\n\n" +
            "Please log in and change your password immediately.\n\n" +
            "Regards,\nThe BookShop Team"
        );
        mailSender.send(message);
    }

}
