package com.rahul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rahul.model.Users;
import com.rahul.repository.UserRepository;
import com.rahul.util.JwtUtil;

@Service
public class AdminService {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void addAdmin(Users users){
       users.setRole("ADMIN");
       users.setPassword(this.passwordEncoder.encode(users.getPassword()));
       this.userRepository.save(users);
    }

    public void promoteToAdmin(String email,String token) throws Exception {
        // Get the authenticated user's username
        String promoterUsername = this.jwtUtil.extractUserName(token);
    
        Users promoter = this.userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Promoter not found"));
    
        if (!promoter.getRole().equals("SUPER_ADMIN")) {
            throw new Exception("Only Super Admins can promote users to Admin.");
        }

        Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public void promoteToSuperAdmin(String token, String email) throws Exception {
        // Get the authenticated user's username
        String authenticatedUsername = this.jwtUtil.extractUserName(token);

        // Fetch the authenticated user from the database
        Users authenticatedUser = userRepository.findByEmail(authenticatedUsername)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Ensure only SUPER_ADMIN can perform this action
        if (!authenticatedUser.getRole().equals("SUPER_ADMIN")) {
            throw new Exception("Only Super Admins can promote users to Super Admin.");
        }

        // Fetch the user to be promoted
        Users userToPromote = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Prevent modification of the first SUPER_ADMIN by anyone else
        if (userToPromote.isFirstSuperAdmin() &&
            !userToPromote.getEmail().equals(authenticatedUser.getEmail())) {
            throw new Exception("The first Super Admin can only be modified by themselves.");
        }

        // Promote the user to SUPER_ADMIN
        userToPromote.setRole("SUPER_ADMIN");
        userRepository.save(userToPromote);
    }


   
    public void deleteUser(String email,String token) throws Exception {
        // Get the authenticated user (Super Admin performing the action)
        String authenticatedUsername =this.jwtUtil.extractUserName(token);

        // Fetch the authenticated user from the database
        Users authenticatedUser = userRepository.findByEmail(authenticatedUsername)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Fetch the user to be deleted
        Users userToDelete = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the user trying to delete is the first Super Admin
        if (userToDelete.isFirstSuperAdmin() && !userToDelete.getEmail().equals(authenticatedUser.getEmail())) {
            throw new Exception("The first Super Admin can only be deleted by themselves.");
        }

        // Ensure that only Super Admins can delete users
        if (!authenticatedUser.getRole().equals("SUPER_ADMIN")) {
            throw new Exception("Only Super Admins can delete users.");
        }

        // Delete the user
        userRepository.delete(userToDelete);
    }
    
    
}
