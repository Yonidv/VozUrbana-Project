package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.DetalleIncidenciaMaterialDto;
import com.example.demo.dtos.IncidenciaDto;
import com.example.demo.model.Incidencia;
import com.example.demo.model.Material;
import com.example.demo.model.Municipalidad;
import com.example.demo.repository.DetalleIncidenciaMaterialRepository;
import com.example.demo.repository.IncidenciaRepository;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.MunicipalidadRepository;
import com.example.demo.service.incidencia.IncidenciaServiceImpl;
import com.example.demo.service.image.ImageService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for IncidenciaServiceImpl")
public class IncidenciaServiceImplTest {
    
    @InjectMocks
    private IncidenciaServiceImpl incidenciaService;

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @Mock
    private DetalleIncidenciaMaterialRepository detalleIncidenciaMaterialRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private MunicipalidadRepository municipalidadRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private MultipartFile imageFile;

    @Test
    @DisplayName("Debe lanzar error cuando el stock es insuficiente al crear una incidencia")
    void createIncidencia_WhenStockIsInsufficient_ShouldThrowRuntimeException() throws Exception {
        Long municipalidadId = 1L;
        Long materialId = 10L;

        Municipalidad municipalidad = new Municipalidad();
        municipalidad.setMunicipalidad_id(municipalidadId);

        Material material = new Material();
        material.setMaterial_id(materialId);
        material.setStock(2);

        Incidencia incidenciaGuardada = new Incidencia();

        when(imageService.storeImage(imageFile)).thenReturn("foto-prueba.jpg");
        when(municipalidadRepository.findById(municipalidadId)).thenReturn(Optional.of(municipalidad));
        when(incidenciaRepository.save(any(Incidencia.class))).thenReturn(incidenciaGuardada);
        when(detalleIncidenciaMaterialRepository.findByIncidencia(any(Incidencia.class))).thenReturn(Collections.emptyList());
        when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));

        IncidenciaDto incidenciaDto = new IncidenciaDto(
                null,
                "Incidencia de prueba",
                null,
                null,
                List.of(new DetalleIncidenciaMaterialDto(null, materialId, 5))
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> incidenciaService.createIncidencia(incidenciaDto, imageFile, municipalidadId));

        assertEquals("La cantidad solicitada excede el stock disponible", exception.getMessage());
        verify(imageService).storeImage(imageFile);
        verify(municipalidadRepository).findById(municipalidadId);
        verify(incidenciaRepository).save(any(Incidencia.class));
        verify(materialRepository, never()).save(any(Material.class));
        verify(detalleIncidenciaMaterialRepository, never()).save(any());
    }
}
