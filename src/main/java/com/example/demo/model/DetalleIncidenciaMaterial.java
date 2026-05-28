package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DetalleIncidenciaMaterial {
    public Long detalle_id;
    public int cantidad_usada;

    
    @ManyToOne(optional = true)
    @JoinColumn(name = "material_id")
    private Material material;

    
    @ManyToOne(optional = true)
    @JoinColumn(name = "municipalidad_id")
    private Municipalidad municipalidad;
}
