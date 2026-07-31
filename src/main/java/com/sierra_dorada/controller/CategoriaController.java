package com.sierra_dorada.controller;
import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.Categoria;
import com.sierra_dorada.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/categorias", "/categorias"})
public class CategoriaController {
    private final CategoriaRepository repository;
    public CategoriaController(CategoriaRepository repository) { this.repository = repository; }
    @GetMapping public List<Categoria> listar() { return repository.findAll(); }
    @GetMapping("/{id}") public Categoria obtener(@PathVariable Integer id) { return buscar(id); }
    @PostMapping public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria item) {
        item.setId(null); return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(item));
    }
    @PutMapping("/{id}") public Categoria actualizar(@PathVariable Integer id, @Valid @RequestBody Categoria item) {
        buscar(id); item.setId(id); return repository.save(item);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        repository.delete(buscar(id)); return ResponseEntity.noContent().build();
    }
    private Categoria buscar(Integer id) { return repository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada")); }
}
