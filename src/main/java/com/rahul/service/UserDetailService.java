package com.rahul.service;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users foundUser;

        // Check if username is an email or a mobile number
        if (username.contains("@")) {
            // Treat as email
            foundUser = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        } else {
            // Treat as mobile number
            foundUser = userRepository.findByContact(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with mobile: " + username));
        }

        return User.builder()
                .username(foundUser.getEmail())
                .password(foundUser.getPassword())
                .roles(foundUser.getRole())
                .build();
    }
}
