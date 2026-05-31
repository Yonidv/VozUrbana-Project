package com.example.demo.security;

import com.example.demo.service.auth.UsuarioLoginService;

import io.jsonwebtoken.MalformedJwtException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class JwtFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioLoginService usuarioLoginService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
                final String authHeader = request.getHeader("Authorization");
                System.out.println("🔍 Verificando encabezado de autorización: " + authHeader); 

                String username = null;
                String jwt = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
             System.out.println("🔐 Token recibido: " + jwt);
            try {
                username = jwtUtil.extraerUsuario(jwt);
            } catch (MalformedJwtException e) {
                System.out.println("⚠️ Token inválido (mal formado): " + jwt);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT inválido");
                return; 
            } catch (Exception e) {
                System.out.println("⚠️ Error al procesar el token JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Error al procesar el token");
                return;
            }
            
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    var userDetails = usuarioLoginService.loadUserByUsername(username);

    if (jwtUtil.validarToken(jwt, userDetails)) {
        String role = jwtUtil.extraerRol(jwt);
        var authorities = role != null && !role.isBlank()
                ? List.of(new SimpleGrantedAuthority(role))
                : userDetails.getAuthorities();

        System.out.println("✅ Token válido para el usuario: " + username);
        System.out.println("✅ Rol del token: " + role);

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("✅ Usuario autenticado en el contexto de seguridad.");
    } else {
        System.out.println("⛔ Token inválido para el usuario: " + username);
    }
} else if (SecurityContextHolder.getContext().getAuthentication() != null) {
    System.out.println("ℹ️ Ya hay una autenticación en el contexto de seguridad.");
}
        filterChain.doFilter(request, response);
    }
}

