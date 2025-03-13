package com.management.management.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.management.management.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsersId(Long userId); 
    Optional<Order> findByIdAndUsersId(Long orderId, Long userId);
}