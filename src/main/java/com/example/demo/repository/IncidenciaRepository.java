package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

	@Query("select i from Incidencia i where i.municipalidad.municipalidad_id = :municipalidadId")
	List<Incidencia> findAllByMunicipalidadId(@Param("municipalidadId") Long municipalidadId);
}
