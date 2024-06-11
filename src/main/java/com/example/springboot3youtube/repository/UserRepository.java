package com.example.springboot3youtube.repository;

import com.example.springboot3youtube.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
  boolean existsByUsername(String userName);
  Optional<User> findByUsername(String userName);
}
