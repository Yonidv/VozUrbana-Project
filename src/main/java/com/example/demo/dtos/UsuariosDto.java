package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosDto {
    private Long usuario_id;
    private String username;
    private String password;
    private String rol;
    private Long municipalidad_id;

}
