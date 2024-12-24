package com.rahul.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rahul.model.Users;
import com.rahul.repository.UserRepository;


@Service
public class UserDetailService implements UserDetailsService {
 @Autowired
 private UserRepository userRepository;

 @Override
 public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
     // Split identifier into email and mobile number
     String[] parts = identifier.split("\\|");
     if (parts.length != 2) {
         throw new UsernameNotFoundException("Invalid identifier format. Use 'email|mobileNo'.");
     }

     String email = parts[0];
     String mobileNo = parts[1];

     // Find user by both email and mobile number
     Optional<Users> user = userRepository.findByEmailAndContact(email, mobileNo);
     if (user.isEmpty()) {
         throw new UsernameNotFoundException("User not found with provided email and mobile number");
     }

     Users foundUser = user.get();

    
     return User.builder()
             .username(foundUser.getEmail())
             .password("")
             .roles(foundUser.getRole())
             .build();
 }
}

