package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    @Query("select u from Usuario u where u.municipalidad.municipalidad_id = :municipalidadId")
    List<Usuario> findAllByMunicipalidadId(@Param("municipalidadId") Long municipalidadId);
}
