package com.example.collab_space.controller;

import com.example.collab_space.requestDto.LoginReqDto;
import com.example.collab_space.requestDto.OtpVerificationDto;
import com.example.collab_space.requestDto.UserRegistrationDto;
import com.example.collab_space.service.OtpService;
import com.example.collab_space.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    OtpService otpService;

    @PostMapping("/registration")
    public ResponseEntity userRegistration(@RequestBody UserRegistrationDto registrationDto){
        try {
            userService.userRegistration(registrationDto);
            return new ResponseEntity("registration successful",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@RequestBody LoginReqDto loginReqDto){
        try {
            if(userService.loginWithEmailPass(loginReqDto)){
                return new ResponseEntity("Logged in",HttpStatus.OK);
            }
        }catch (RuntimeException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @PostMapping("/login/{email}")
    public ResponseEntity userLoginWithOtp(@PathVariable String email){
        try {
                userService.loginWithOtp(email);
                return new ResponseEntity("Otp is sent to your email",HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/otp/verify")
    public ResponseEntity<String> otpVerification(@RequestBody OtpVerificationDto verificationDto){
        try {
            if(otpService.verify(verificationDto)) {
                return new ResponseEntity<>("Otp is verified", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Invalid Otp",HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
