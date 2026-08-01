package com.sierra_dorada.controller;

import com.sierra_dorada.model.Pedido;
import com.sierra_dorada.service.PedidoService;
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
@RequestMapping({"/api/pedidos", "/pedidos"})
public class PedidoController {
    private final PedidoService servicio;

    public PedidoController(PedidoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) Integer usuarioId) {
        return servicio.listar(usuarioId);
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable Integer id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(pedido));
    }

    @PutMapping("/{id}")
    public Pedido actualizar(@PathVariable Integer id, @Valid @RequestBody Pedido pedido) {
        return servicio.actualizar(id, pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
