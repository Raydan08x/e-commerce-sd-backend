package com.sierra_dorada.controller;

import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.Pago;
import com.sierra_dorada.repository.PagoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoRepository repositorio;

    public PagoController(PagoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Pago> listar(@RequestParam(required = false) Integer pedidoId) {
        return pedidoId == null ? repositorio.findAll() : repositorio.findByPedidoId(pedidoId);
    }

    @GetMapping("/{id}")
    public Pago obtener(@PathVariable Integer id) {
        return buscar(id);
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody Pago pago) {
        pago.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repositorio.save(pago));
    }

    @PutMapping("/{id}")
    public Pago actualizar(@PathVariable Integer id, @Valid @RequestBody Pago pago) {
        buscar(id);
        pago.setId(id);
        return repositorio.save(pago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        repositorio.delete(buscar(id));
        return ResponseEntity.noContent().build();
    }

    private Pago buscar(Integer id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado"));
    }
}
