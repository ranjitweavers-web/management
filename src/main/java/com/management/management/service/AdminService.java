package com.management.management.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.management.management.model.Product;
import com.management.management.model.User;
import com.management.management.repositories.ProductRepository;
import com.management.management.repositories.UserRepository;
import com.management.management.security.PasswordEncoder;

@Service
public class AdminService {

    // Get all users

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ProductRepository productRepository;
    

    public List<User> getAllUsers() {
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
                user.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(updatedUser.getPassword())); // Encode
                                                                                                             // the
                                                                                                             // password
            }
            if (updatedUser.getRole() != null) {
                user.setRole(updatedUser.getRole());
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + id);
        }
    }

    // Delete User

    public String deleteUser(Long id) {
        Optional<?> findUser = userRepository.findById(id);
        if (findUser.isPresent()) {
            userRepository.deleteById(id);
            return "delete successfully";

        } else {
            return "failed to delete user";
        }

    }

    // Create Products Admin
    public Product createProduct(Product product) {
        return productRepository.save(product);

    }

    // Get all products by Admin

    public Set<Product> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return new HashSet<>(allProducts);
    }

    // Delete products by Admin

    public String deleteProducts(Long id) {
        Optional<?> getProduct = productRepository.findById(id);
        if (getProduct.isPresent()) {
            productRepository.deleteById(id);
            return "Product Deletion Successfully";
        } else {
            return "Failed to Delete the Product";
        }

    }

    // Update Products by Admin

    public Product updateProduct(Long id, Product product) {
        Optional<?> getProduct = userRepository.findById(id);
        if (getProduct.isPresent()) {
            product.setName(product.getName());
            product.setPrice(product.getPrice());
            product.setQuantity(product.getQuantity());
            return productRepository.save(product);
            
        }else{
            return product;
        }
        

    }

   

}
