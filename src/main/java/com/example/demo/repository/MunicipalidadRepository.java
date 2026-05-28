package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Municipalidad;

public interface MunicipalidadRepository extends JpaRepository<Municipalidad, Long> {
    
}
