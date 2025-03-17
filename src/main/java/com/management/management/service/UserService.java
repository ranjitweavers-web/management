package com.management.management.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.management.management.model.Order;
import com.management.management.model.Product;
import com.management.management.model.User;
import com.management.management.DTO.LoginRequest;
import com.management.management.DTO.Status;
import com.management.management.configs.SecurityConfig;
import com.management.management.repositories.OrderRepository;
import com.management.management.repositories.ProductRepository;
import com.management.management.repositories.UserRepository;
import com.management.management.security.SecuriyConfigHelper;
import com.management.management.util.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SecuriyConfigHelper securiyConfigHelper;
    @Autowired
    JwtUtil jwtUtil;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
    }

    //
    // For Register New User
    public Map<Status, ResponseEntity<User>> registerUser(User user) {
        Map<Status, ResponseEntity<User>> response = new HashMap<>();

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            response.put(Status.FAILED, ResponseEntity.status(HttpStatus.CONFLICT).build());
            return response;
        }
        user.setPassword(securiyConfigHelper.bCryptPasswordEncoder().encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        emailService.sendEmail(savedUser.getEmail(), "Welcome!",
                "Hello " + savedUser.getUsername() + ", Your Account has been created successfully."
                        + "\nEmail: " + savedUser.getEmail()
                        + "\nRole: " + savedUser.getRole());

        response.put(Status.SUCCESSFULL, ResponseEntity.status(HttpStatus.CREATED).body(savedUser));
        return response;
    }

    // For Login
    public Map<String,String> loginUser(LoginRequest LoginRequest) {
        Map<String,String> loginMap = new HashMap<>();
        Optional<User> user = userRepository.findByEmail(LoginRequest.getEmail());
        if (user.isPresent()
                && securiyConfigHelper.bCryptPasswordEncoder().matches(LoginRequest.getPassword(),
                        user.get().getPassword())) {
                            loginMap.put("Login Successfull", jwtUtil.generateToken(LoginRequest.getEmail()));
                 return loginMap;
        }

        else {
            throw new RuntimeException("Invalid email or password");
        }

    }

    // For Getting User Details

    public String userDetails(String email, String password) {
        Optional<User> userDetails = userRepository.findByEmail(email);
        if (userDetails.isPresent()) {
            return "User Details are " + userDetails.get().getUsername() + " and " + userDetails.get().getRole()
                    + " and " + userDetails.get().getEmail() + " ";
        }
        return password;

    }

    // make order
    public String makeOrder(Long userId, Order order) {
        // Check if user exists
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // Check if product exists
        Optional<Product> optionalProduct = productRepository.findById(order.getProduct().getId());
        if (optionalProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + order.getProduct().getId());
        }

        User user = optionalUser.get();
        Product product = optionalProduct.get();
        if (order.getQuantity() < 1) {
            return "Invalid Quantity" + " -> " + order.getQuantity();
        }

        // Ensure sufficient stock
        if (product.getQuantity() < order.getQuantity()) {
            return "Insufficient stock! Available: " + product.getQuantity();
        }

        // Reduce product stock
        product.setQuantity(product.getQuantity() - order.getQuantity());
        productRepository.save(product);

        // Set user and product in order
        order.setUsers(user);
        order.setProduct(product);
        orderRepository.save(order);

        return "Order placed successfully! Remaining stock: " + product.getQuantity();
    }

    // See All My Own Orders

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUsersId(userId);

    }

    // Delete Own Orders
    public String deleteOrder(Long orderId, Long userId) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUsersId(orderId, userId);

        if (optionalOrder.isPresent()) {
            orderRepository.delete(optionalOrder.get());
            return "Order deleted successfully!";
        } else {
            return "Order not found or you do not have permission to delete it.";
        }
    }
    // Update Own Order

    public String updateOrder(Long orderId, Long userId, Order updatedOrder) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndUsersId(orderId, userId);

        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();

            // Update order details
            existingOrder.setProduct(updatedOrder.getProduct());
            existingOrder.setQuantity(updatedOrder.getQuantity());
            existingOrder.setStatus(updatedOrder.getStatus());

            orderRepository.save(existingOrder);
            return "Order updated successfully!";
        } else {
            return "Order not found or does not belong to the user.";
        }
    }

    // See all Products

    public List<Product> seeAllProducts() {
        return productRepository.findAll();
    }

}
