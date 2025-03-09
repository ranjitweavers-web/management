package com.management.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.management.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);

}
