package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long detalle_id;
    public int cantidad_usada;

    @ManyToOne(optional = true)
    @JoinColumn(name = "incidencia_id")
    private Incidencia incidencia;

    
    @ManyToOne(optional = true)
    @JoinColumn(name = "material_id")
    private Material material;

}
