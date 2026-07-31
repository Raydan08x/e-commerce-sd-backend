package com.sierra_dorada.controller;
import com.sierra_dorada.model.Pedido;
import com.sierra_dorada.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/pedidos", "/pedidos"})
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }
    @GetMapping public List<Pedido> listar(@RequestParam(required=false) Integer usuarioId) { return service.listar(usuarioId); }
    @GetMapping("/{id}") public Pedido obtener(@PathVariable Integer id) { return service.obtener(id); }
    @PostMapping public ResponseEntity<Pedido> crear(@Valid @RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(pedido));
    }
    @PutMapping("/{id}") public Pedido actualizar(@PathVariable Integer id, @Valid @RequestBody Pedido pedido) {
        return service.actualizar(id, pedido);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id); return ResponseEntity.noContent().build();
    }
}
