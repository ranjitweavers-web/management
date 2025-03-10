package com.management.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.management.model.User;
import com.management.management.repositories.UserRepository;
import com.management.management.security.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user){ 
        // Encode password before saving 
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        User saveUser = userRepository.save(user);

        // now send welcome email
        emailService.sendEmail(user.getEmail(), "Welcome !", "Hello "+ user.getUsername() + ", Your Account has been created successfully");
        return saveUser;
        
    }
}
