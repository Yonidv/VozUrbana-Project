package com.example.demo.service.municipalidad;

import java.util.List;

import com.example.demo.dtos.MunicipalidadDto;
import com.example.demo.model.Municipalidad;

public interface MunicipalidadService {
    Municipalidad save(MunicipalidadDto municipalidadDTO);
    MunicipalidadDto findById(Long id);
    Municipalidad update(Long id, MunicipalidadDto municipalidadDTO);
    void delete(Long id);
    List<MunicipalidadDto> findAll();
}
