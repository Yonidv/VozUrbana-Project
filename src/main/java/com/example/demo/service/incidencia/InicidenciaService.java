package com.example.demo.service.incidencia;

import java.util.List;

import com.example.demo.model.Incidencia;

public interface InicidenciaService {
    Incidencia createIncidencia(Incidencia incidencia);
    Incidencia getIncidenciaById(Long id);
    Incidencia updateIncidencia(Long id, Incidencia incidencia);
    void deleteIncidencia(Long id);
    List<Incidencia> getAllIncidencias();
    
}
