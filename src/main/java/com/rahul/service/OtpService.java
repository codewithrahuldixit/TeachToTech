package com.rahul.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rahul.model.Users;
import com.rahul.repository.UserRepository;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    // Generate a random 6-digit OTP
    public String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Save OTP to the database and send email
    public void sendOtpEmail(String email) {
        String otp = this.generateOtp();

        // Save OTP to the database
        Optional<Users> user =this.userRepository.findByEmail(email);
        Users user1=user.get();
        user1.setOtp(otp);
        user1.setCreatedAt(LocalDateTime.now());
        user1.setOtpUsed(false);
        this.userRepository.save(user1);
        

        // Send OTP via email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\n\nPlease do not share this with anyone.");
        mailSender.send(message);
    }

    // Validate the OTP
    public boolean validateOtp(String email, String otp) {
        Optional<Users> user = this.userRepository.findByEmail(email);

        if (user.isPresent()) {
           Users user1=user.get();

            // Check if OTP is still valid (e.g., within 5 minutes)
             if (user1.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
                user1.setOtpUsed(true); // Mark OTP as used
                this.userRepository.save(user1);
                return true;
            }
        }
       else{
        throw new IllegalArgumentException("No user found with the provided email address.");
       } 
       return false;
    }
}

