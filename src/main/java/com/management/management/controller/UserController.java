package com.management.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management.DTO.LoginRequest;
import com.management.management.model.Order;
import com.management.management.model.User;
import com.management.management.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public String registerUser(@RequestBody User user){
        return userService.registerUser(user);


    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest.getEmail() , loginRequest.getPassword());
       
    }

    @GetMapping("/userDetails")
    public String getUserDetails(@RequestParam LoginRequest loginRequest) {
        return userService.userDetails(loginRequest.getEmail(), loginRequest.getPassword());
    }
    

    @PostMapping("/makeOrder/{userId}")
    public ResponseEntity<String> makeOrder(@PathVariable Long userId, @RequestBody Order order) {
    String response = userService.makeOrder(userId, order);
    return ResponseEntity.ok(response);
}


@GetMapping("/myOrders/{userId}")
public List<Order> getMyOrders(@PathVariable Long userId){
  return userService.getOrdersByUserId(userId);

}


    



}
