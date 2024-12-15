package com.rahul.service;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import com.rahul.repository.UserRepository;
import com.rahul.model.Users;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {
    
 private final UserRepository userRepository;

  @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<Users> user=this.userRepository.findByEmail(email);
       if(user.isEmpty()){
        throw new UsernameNotFoundException("User does not exit");
       }
       Users m=user.get();
       return  User.builder().username(m.getEmail())
                   .password(m.getPassword()).roles(m.getRole()).build();
   
    }

}