package com.management.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.management.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
