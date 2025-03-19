package com.management.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query(value = "SELECT * FROM Product WHERE id = :productId", nativeQuery = true)
  Product findProductById(@Param("productId") Long id);

  @Query(value = "SELECT COUNT(*) FROM Product", nativeQuery = true)
  Long numberOfRecords();
}
