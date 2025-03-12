package com.management.management.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.management.management.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUsersId(Long userId);  // Correct method name
}