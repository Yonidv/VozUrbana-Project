package com.example.demo.service.municipalidad;

import java.util.List;

import com.example.demo.model.Municipalidad;

public interface MunicipalidadService {
    Municipalidad findById(Long id);
    Municipalidad save(Municipalidad municipalidad);
    Municipalidad update(Long id, Municipalidad municipalidad);
    void delete(Long id);
    List<Municipalidad> findAll();
}
