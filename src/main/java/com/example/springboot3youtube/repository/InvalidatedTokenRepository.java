package com.example.springboot3youtube.repository;

import com.example.springboot3youtube.entity.InvalidatedToken;
import com.example.springboot3youtube.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
}
