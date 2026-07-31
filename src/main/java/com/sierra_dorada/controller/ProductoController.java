package com.sierra_dorada.controller;
import com.sierra_dorada.model.Producto;
import com.sierra_dorada.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping({"/api/productos", "/productos"})
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService service) { this.service = service; }
    @GetMapping public List<Producto> listar(@RequestParam(defaultValue="true") boolean soloActivos,
        @RequestParam(required=false) String buscar, @RequestParam(required=false) Integer categoriaId) {
        return service.listar(soloActivos, buscar, categoriaId);
    }
    @GetMapping("/{id}") public Producto obtener(@PathVariable Integer id) { return service.obtener(id); }
    @PostMapping public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(producto));
    }
    @PutMapping("/{id}") public Producto actualizar(@PathVariable Integer id, @Valid @RequestBody Producto producto) {
        return service.actualizar(id, producto);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id); return ResponseEntity.noContent().build();
    }
}
