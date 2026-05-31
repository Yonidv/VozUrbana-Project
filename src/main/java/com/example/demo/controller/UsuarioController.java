package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.UsuariosDto;
import com.example.demo.model.Usuario;
import com.example.demo.service.usuario.UsuarioService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UsuariosDto usuarioDto) {
        HashMap<String, Object> response = new HashMap<>();
        Usuario newUser = usuarioService.createUser(usuarioDto);
        if (newUser == null) {
            response.put("mensaje", "Error al crear el usuario");
        } else {
            response.put("mensaje", "Usuario creado exitosamente");
        }
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UsuariosDto usuarioDto) {
        HashMap<String, Object> response = new HashMap<>();
        Usuario userUpdate = usuarioService.updateUser(id, usuarioDto);
        if (userUpdate == null) {
            response.put("mensaje", "Error al actualizar el usuario");
        } else {
            response.put("mensaje", "Usuario actualizado exitosamente");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Long id) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            usuarioService.deleteUser(id);
            response.put("mensaje", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            response.put("mensaje", "Error al eliminar el usuario: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UsuariosDto>> seeAllUsers() {
        return ResponseEntity.ok(usuarioService.getAllUsers());
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<UsuariosDto> searchUserById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUserById(id));
    }
}
