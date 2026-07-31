package com.sierra_dorada.controller;
import com.sierra_dorada.model.Usuario;
import com.sierra_dorada.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/usuarios", "/usuarios"})
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public List<Usuario> listar() { return service.listar(); }
    @GetMapping("/{id}") public Usuario obtener(@PathVariable Integer id) { return service.obtener(id); }
    @PostMapping public ResponseEntity<Usuario> crear(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(usuario));
    }
    @PutMapping("/{id}") public Usuario actualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        return service.actualizar(id, usuario);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id); return ResponseEntity.noContent().build();
    }
}
