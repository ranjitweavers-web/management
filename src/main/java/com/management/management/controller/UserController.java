package com.management.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management.DTO.LoginRequest;
import com.management.management.DTO.Status;
import com.management.management.model.Order;
import com.management.management.model.Product;
import com.management.management.model.User;
import com.management.management.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Register New user
    @PostMapping("/register")
    public Map<Status,ResponseEntity<User> >registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // Login controller
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

    }

    // Get user Details

    @GetMapping("/userDetails")
    public String getUserDetails(@RequestParam LoginRequest loginRequest) {
        return userService.userDetails(loginRequest.getEmail(), loginRequest.getPassword());
    }

    // Make new Order
    @PostMapping("/makeOrder/{userId}")
    public ResponseEntity<String> makeOrder(@PathVariable Long userId, @RequestBody Order order) {
        String response = userService.makeOrder(userId, order);
        return ResponseEntity.ok(response);
    }

    // Get Own Orders
    @GetMapping("/myOrders/{userId}")
    public List<Order> getMyOrders(@PathVariable Long userId) {
        return userService.getOrdersByUserId(userId);

    }

    // Delete Order
    @DeleteMapping("myOrder/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId, @RequestParam Long userId) {
        String response = userService.deleteOrder(orderId, userId);
        return ResponseEntity.ok(response);
    }

    // Update Order

    @PutMapping("myOrder/update/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable Long orderId, @RequestParam Long userId,
            @RequestBody Order updatedOrder) {
        String response = userService.updateOrder(orderId, userId, updatedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allProducts")
    public List<Product> allProducts() {
        return userService.seeAllProducts();
    }
    

}
