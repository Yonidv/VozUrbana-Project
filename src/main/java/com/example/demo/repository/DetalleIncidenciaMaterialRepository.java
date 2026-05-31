package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DetalleIncidenciaMaterial;
import com.example.demo.model.Incidencia;

public interface DetalleIncidenciaMaterialRepository extends JpaRepository<DetalleIncidenciaMaterial, Long> {
    List<DetalleIncidenciaMaterial> findByIncidencia(Incidencia incidencia);
}