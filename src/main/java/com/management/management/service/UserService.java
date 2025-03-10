package com.management.management.service;

import java.util.Optional;

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

    // For Register New User
    public String registerUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        // now send welcome email
        emailService.sendEmail(user.getEmail(), "Welcome !",
                "Hello " + user.getUsername() + ", Your Account has been created successfully"
                        + "Your information are \n" + user.getEmail() + "\n Your Role is " + user.getRole());
        return "User Register Successfully!";

    }

    // For Login
    public String loginUser(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()
                && passwordEncoder.bCryptPasswordEncoder().matches(rawPassword, user.get().getPassword())) {
            return "Login Successfull!";

        }

        else {
            throw new RuntimeException("Invalid email or password");
        }

    }


    // For Getting User Details

    
    public String userDetails(String email, String password){
        Optional<User> userDetails = userRepository.findByEmail(email);
        if(userDetails.isPresent()){
             return "User Details are " + userDetails.get().getUsername() + " and " + userDetails.get().getRole() + " and " + userDetails.get().getEmail() + " ";        }
                     return password;
                     

         
    }
}
