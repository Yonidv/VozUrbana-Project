package com.example.demo.dtos;

import com.example.demo.enums.EstadoIncidencia;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidenciaDto {
    private Long incidencia_id;
    private String descripcion;
    private String foto;
    private EstadoIncidencia estado;
    private List<DetalleIncidenciaMaterialDto> detalles;
}
