package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Material;

public interface MaterialRepository extends JpaRepository<Material, Long> {

	@Query("select m from Material m where m.municipalidad.municipalidad_id = :municipalidadId")
	List<Material> findAllByMunicipalidadId(@Param("municipalidadId") Long municipalidadId);

	@Query(value = "SELECT TOP 1 codigo_material FROM material ORDER BY TRY_CAST(SUBSTRING(codigo_material, 5, 20) AS INT) DESC", nativeQuery = true)
	Optional<String> findTopCodigoMaterial();
}
