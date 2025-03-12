package com.management.management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.management.model.Order;
import com.management.management.model.Product;
import com.management.management.model.User;
import com.management.management.repositories.OrderRepository;
import com.management.management.repositories.ProductRepository;
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
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

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

        // Ensure sufficient stock
        if (product.getQuantity() < order.getQuantity()) {
            return "Insufficient stock! Available: " + product.getQuantity();
        }

        // Reduce product stock
        product.setQuantity(product.getQuantity() - order.getQuantity());
        productRepository.save(product); // Save updated product stock

        // Set user and product in order
        order.setUsers(user);
        order.setProduct(product);
        orderRepository.save(order); // Save order

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

    public List<Product> seeAllProducts(){
         return productRepository.findAll();
    }


}
