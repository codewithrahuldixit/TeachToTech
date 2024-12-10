package com.rahul.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.model.User;
import com.rahul.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

   

    public User registerUser(User user) {
        user.setPassword(Math.random());
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
