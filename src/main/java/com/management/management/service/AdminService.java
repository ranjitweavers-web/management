package com.management.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.management.model.User;
import com.management.management.repositories.UserRepository;
import com.management.management.security.PasswordEncoder;


@Service
public class AdminService {

    // Get all users

    @Autowired
     UserRepository userRepository;
     @Autowired
     PasswordEncoder passwordEncoder;

     public List<User> getAllUsers(){
        return userRepository.findAll();
        
     }

     // Update Users Details

     public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            // Update only if new values are provided
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(updatedUser.getPassword())); // Encode the password
            }
            if (updatedUser.getRole() != null) {
                user.setRole(updatedUser.getRole());
            }
    
            return userRepository.save(user); 
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }
    


}
