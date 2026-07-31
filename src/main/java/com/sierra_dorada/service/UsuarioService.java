package com.sierra_dorada.service;

import com.sierra_dorada.exception.*;
import com.sierra_dorada.model.Usuario;
import com.sierra_dorada.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Usuario> listar() { return repository.findAll(); }
    public Usuario obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }
    public Usuario crear(Usuario usuario) {
        if (repository.existsByEmailIgnoreCase(usuario.getEmail())) throw new ConflictoException("El email ya está registrado");
        usuario.setId(null);
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return repository.save(usuario);
    }
    public Usuario actualizar(Integer id, Usuario datos) {
        Usuario actual = obtener(id);
        if (!actual.getEmail().equalsIgnoreCase(datos.getEmail()) && repository.existsByEmailIgnoreCase(datos.getEmail()))
            throw new ConflictoException("El email ya está registrado");
        actual.setNombres(datos.getNombres()); actual.setApellidos(datos.getApellidos());
        actual.setFechaNacimiento(datos.getFechaNacimiento()); actual.setGenero(datos.getGenero());
        actual.setDireccion(datos.getDireccion()); actual.setEmail(datos.getEmail().toLowerCase());
        actual.setTelefono(datos.getTelefono()); actual.setActivo(datos.getActivo());
        if (datos.getRol() != null) actual.setRol(datos.getRol());
        if (datos.getContrasena() != null && !datos.getContrasena().isBlank())
            actual.setContrasena(passwordEncoder.encode(datos.getContrasena()));
        return repository.save(actual);
    }
    public void eliminar(Integer id) { repository.delete(obtener(id)); }
}
