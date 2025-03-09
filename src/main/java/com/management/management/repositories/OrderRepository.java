package com.management.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.management.model.Order;

public interface OrderRepository extends JpaRepository<Order , Long> {

}
