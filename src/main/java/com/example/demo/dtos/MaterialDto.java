package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDto {
    
    @JsonProperty("material_id")
    private Long material_id;
    
    @JsonProperty("codigo_material")
    private String codigo_material;
    
    @JsonProperty("nombre_material")
    private String nombre_material;
    
    private Integer stock; 
    
    @JsonProperty("municipalidad_id")
    private Long municipalidad_id;

}