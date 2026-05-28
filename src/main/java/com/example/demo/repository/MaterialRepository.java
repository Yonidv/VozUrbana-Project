package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    
}
