package com.management.management.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management.model.Order;
import com.management.management.model.Product;
import com.management.management.model.User;
import com.management.management.service.AdminService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {

        return adminService.updateUser(id, user);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId) {

        return adminService.deleteUser(userId);
    }

    // Create Product by Admin

    @RequestMapping("/product/createProduct")
    public String createProduct(@RequestBody Product product) {
        return adminService.createProduct(product);
    }

    // Get All Products

    @GetMapping("/product/getAllProducts")
    public Set<Product> getAllProducts() {
        return adminService.getAllProducts();

    }

    // Delete Product

    @DeleteMapping("/product/deleteProduct/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        return adminService.deleteProducts(productId);

    }

    // Update Product
    @PutMapping("/product/updateProduct/{id}")
    public String updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return adminService.updateProduct(id, product);
    }

    // Get all Orders

    @GetMapping("/getAllOrders")
    public List<Order> getAllOrders() {
        return adminService.allOrders();
    }

}
