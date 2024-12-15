package com.rahul.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rahul.model.Users;
import com.rahul.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
   

    public Users registerUser(Users user) {
        user.setRole("USER");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
