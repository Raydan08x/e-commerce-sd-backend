package com.sierra_dorada.controller;
import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.Pago;
import com.sierra_dorada.repository.PagoRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoRepository repository;
    public PagoController(PagoRepository repository) { this.repository = repository; }
    @GetMapping public List<Pago> listar(@RequestParam(required=false) Integer pedidoId) {
        return pedidoId == null ? repository.findAll() : repository.findByPedidoId(pedidoId);
    }
    @GetMapping("/{id}") public Pago obtener(@PathVariable Integer id) { return buscar(id); }
    @PostMapping public ResponseEntity<Pago> crear(@Valid @RequestBody Pago item) {
        item.setId(null); return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(item));
    }
    @PutMapping("/{id}") public Pago actualizar(@PathVariable Integer id, @Valid @RequestBody Pago item) {
        buscar(id); item.setId(id); return repository.save(item);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        repository.delete(buscar(id)); return ResponseEntity.noContent().build();
    }
    private Pago buscar(Integer id) { return repository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado")); }
}
