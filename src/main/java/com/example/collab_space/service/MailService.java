package com.example.collab_space.service;

import com.example.collab_space.model.User;
import com.example.collab_space.model.Workspace;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendOtp(String email,Integer otp){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Otp Verification");
        mailMessage.setText(String.valueOf(otp));

        mailSender.send(mailMessage);
    }

    public void inviteExistingUser(User user, Workspace workspace, String userEmail, UUID inviteToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Invite email");
        mailMessage.setText("http://127.0.0.1:5500/accept.html?token="+inviteToken);
        mailSender.send(mailMessage);
    }

    public void inviteNonExistingUser(User user, Workspace workspace, String userEmail, UUID inviteToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Invite email");
        mailMessage.setText("http://127.0.0.1:5500/signup.html?token="+inviteToken);
        mailSender.send(mailMessage);
    }

}
