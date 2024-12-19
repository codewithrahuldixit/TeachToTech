package com.rahul.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rahul.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,UUID> {
    // Boolean existsByUsername(String username);
    // Boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndContact(String email, String contact);
   
}
