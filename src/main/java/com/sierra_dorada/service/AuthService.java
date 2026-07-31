package com.sierra_dorada.service;

import com.sierra_dorada.dto.*;
import com.sierra_dorada.model.Usuario;
import com.sierra_dorada.repository.UsuarioRepository;
import com.sierra_dorada.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarios;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository usuarios,
                       UsuarioService usuarioService, JwtService jwtService) {
        this.authenticationManager = authenticationManager; this.usuarios = usuarios;
        this.usuarioService = usuarioService; this.jwtService = jwtService;
    }
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usuario().toLowerCase(), request.password()));
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }
        Usuario usuario = usuarios.findByEmailIgnoreCase(request.usuario())
            .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos"));
        return respuesta(usuario);
    }
    public AuthResponse registrar(RegistroRequest request) {
        if (request.fechaNacimiento().isAfter(LocalDate.now().minusYears(18)))
            throw new IllegalArgumentException("Debes ser mayor de 18 años para crear una cuenta");
        Usuario usuario = new Usuario();
        usuario.setNombres(request.nombre()); usuario.setApellidos(request.apellidos());
        usuario.setFechaNacimiento(request.fechaNacimiento()); usuario.setGenero(request.genero());
        usuario.setDireccion(request.direccion()); usuario.setTelefono(request.telefono());
        usuario.setEmail(request.email().toLowerCase()); usuario.setContrasena(request.password());
        return respuesta(usuarioService.crear(usuario));
    }
    private AuthResponse respuesta(Usuario usuario) {
        return new AuthResponse(jwtService.generar(usuario), "Bearer", usuario.getId(), usuario.getEmail(),
            usuario.getNombres() + " " + usuario.getApellidos(), usuario.getRol().name().toLowerCase());
    }
}
