package com.example.collab_space.service;

import com.example.collab_space.model.Otp;
import com.example.collab_space.model.User;
import com.example.collab_space.repository.OtpRepository;
import com.example.collab_space.repository.UserRepository;
import com.example.collab_space.requestDto.LoginReqDto;
import com.example.collab_space.requestDto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OtpRepository otpRepository;

    @Autowired
    OtpService otpService;

    @Autowired
    MailService mailService;

    public void userRegistration(UserRegistrationDto registrationDto){
        User user1 = userRepository.findByEmail(registrationDto.getEmail());

        if(user1 != null && user1.isActive()){
            throw new RuntimeException("User with this email already exists");
        }
        Otp otp = null;
        if(user1 != null && !user1.isActive()){
            otp = otpService.renewOtp(user1);
            mailService.sendOtp(user1.getEmail(), otp.getOtp());
        }else {
            User user = new User();
            user.setActive(false);
            user.setName(registrationDto.getName());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(registrationDto.getPassword());
            userRepository.save(user);
            otp = otpService.generateOtp(user);
            otpRepository.save(otp);
            mailService.sendOtp(user.getEmail(), otp.getOtp());
        }
    }

    public boolean loginWithEmailPass(LoginReqDto loginReqDto) {
        User user = userRepository.findByEmail(loginReqDto.getEmail());

        if(user == null){
            throw new RuntimeException("User not found");
        }

        if(!user.isActive()){
            throw new RuntimeException("User not verified, please register with this email again");
        }

        if(user.getPassword().equals(loginReqDto.getPassword())){
            return true;
        }else {
            throw new RuntimeException("Incorrect credentials");
        }
    }

    public void loginWithOtp(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new RuntimeException("User not found");
        }

        if(!user.isActive()){
            throw new RuntimeException("User not verified, please register with this email again");
        }
        Otp otp = otpService.renewOtp(user);
        mailService.sendOtp(email, otp.getOtp());
    }
}
