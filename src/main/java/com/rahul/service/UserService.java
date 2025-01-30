package com.rahul.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        user.setRole("USER"); // Subsequent users are regular users
        user.setFirstSuperAdmin(false);
    
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
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

    public Optional<Users> findByContact(String contact) {
      return this.userRepository.findByContact(contact);
    }

    public boolean updatePassword(String email, String newPassword) {
        Optional<Users> user = this.userRepository.findByEmail(email);
        Users user1=user.get();
        user1.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user1);
        return true;
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> findById(UUID id) {
        return userRepository.findById(id);
    }
    public void deleteUser(UUID Id) {
        this.userRepository.deleteById(Id);
    }
    public Users updateUser(String email, Users updatedUsers) {
        // Retrieve the existing user from the repository
        Optional<Users> optionalUser = this.userRepository.findByEmail(email);
    
        // Check if the user exists
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
    
            // Update the user details with the new data
            existingUser.setFirstName(updatedUsers.getFirstName());
            existingUser.setLastName(updatedUsers.getLastName());
            existingUser.setContact(updatedUsers.getContact());
            existingUser.setEmail(updatedUsers.getEmail());
            existingUser.setDob(updatedUsers.getDob());
            existingUser.setQualification(updatedUsers.getQualification());
            existingUser.setRole(updatedUsers.getRole());
    
            // Save and return the updated user
            return this.userRepository.save(existingUser);
        } else {
            // Handle case where user is not found
            throw new RuntimeException("User with id " + email + " not found");
        }
    }
    
}
