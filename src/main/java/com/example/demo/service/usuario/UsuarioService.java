package com.example.demo.service.usuario;

import java.util.List;

import com.example.demo.dtos.UsuariosDto;
import com.example.demo.model.Usuario;

public interface UsuarioService {
    Usuario createUser(UsuariosDto usuarioDto, Long municipalidadId);
    UsuariosDto getUserById(Long id, Long municipalidadId);
    Usuario updateUser(Long id, UsuariosDto usuarioDto, Long municipalidadId);
    void deleteUser(Long id, Long municipalidadId);
    List<UsuariosDto> getAllUsers(Long municipalidadId);

}
