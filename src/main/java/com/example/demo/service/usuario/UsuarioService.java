package com.example.demo.service.usuario;

import java.util.List;

import com.example.demo.dtos.UsuariosDto;
import com.example.demo.model.Usuario;

public interface UsuarioService {
    Usuario createUser(UsuariosDto usuarioDto);
    UsuariosDto getUserById(Long id);
    Usuario updateUser(Long id, UsuariosDto usuarioDto);
    void deleteUser(Long id);
    List<UsuariosDto> getAllUsers();

}
