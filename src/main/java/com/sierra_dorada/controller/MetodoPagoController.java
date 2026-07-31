package com.sierra_dorada.controller;
import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.MetodoPago;
import com.sierra_dorada.repository.MetodoPagoRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {
    private final MetodoPagoRepository repository;
    public MetodoPagoController(MetodoPagoRepository repository) { this.repository = repository; }
    @GetMapping public List<MetodoPago> listar(@RequestParam(defaultValue="true") boolean soloActivos) {
        return soloActivos ? repository.findByActivoTrue() : repository.findAll();
    }
    @GetMapping("/{id}") public MetodoPago obtener(@PathVariable Integer id) { return buscar(id); }
    @PostMapping public ResponseEntity<MetodoPago> crear(@Valid @RequestBody MetodoPago item) {
        item.setId(null); return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(item));
    }
    @PutMapping("/{id}") public MetodoPago actualizar(@PathVariable Integer id, @Valid @RequestBody MetodoPago item) {
        buscar(id); item.setId(id); return repository.save(item);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        repository.delete(buscar(id)); return ResponseEntity.noContent().build();
    }
    private MetodoPago buscar(Integer id) { return repository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado")); }
}
