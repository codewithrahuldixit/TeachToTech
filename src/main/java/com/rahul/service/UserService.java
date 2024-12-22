package com.rahul.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import com.rahul.model.Users;
import com.rahul.repository.UserRepository;
import com.rahul.util.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
   

    public Users registerUser(Users user) {
        long existingUserCount = userRepository.count(); // Check the number of existing users
    
        // Set role and firstSuperAdmin flag based on whether it's the first user
        if (existingUserCount == 0) {
            user.setRole("SUPER_ADMIN"); // First user becomes Super Admin
            user.setFirstSuperAdmin(true);
        } else {
            user.setRole("USER"); // Subsequent users are regular users
            user.setFirstSuperAdmin(false);
        }
    
        // Encode the password before saving the user
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    
        // Save the user and return the saved instance
        return userRepository.save(user);
    }
    
    public String getUsersDetails(String token){
       String username=this.jwtUtil.extractUserName(token);
       Optional<Users> user=this.userRepository.findByEmail(username);

       return user.get().getFirstName();
    }


    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
