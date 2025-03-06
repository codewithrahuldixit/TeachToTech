package com.rahul.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rahul.model.Users;
import com.rahul.repository.UserRepository;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, ScheduledFuture<?>> otpExpirations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
    public void sendOtpEmailBefore(String email) {
        String otp = this.generateOtp();        
        // Send OTP via email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\n\nPlease do not share this with anyone.");
        mailSender.send(message);
        this.storeOtp(email, otp);
    }
    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);

        // Remove OTP after 5 minutes
        ScheduledFuture<?> expirationTask = scheduler.schedule(() -> {
            otpStorage.remove(email);
            otpExpirations.remove(email);
        }, 5, TimeUnit.MINUTES);

        // If an OTP already exists, cancel the previous expiration task
        if (otpExpirations.containsKey(email)) {
            otpExpirations.get(email).cancel(false);
        }

        otpExpirations.put(email, expirationTask);
    }

    public boolean validateBeforeOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));
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

