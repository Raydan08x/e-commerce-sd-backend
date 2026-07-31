package com.sierra_dorada.service;

import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.*;
import com.sierra_dorada.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final CategoriaRepository categorias;
    public ProductoService(ProductoRepository repository, CategoriaRepository categorias) {
        this.repository = repository; this.categorias = categorias;
    }
    public List<Producto> listar(boolean soloActivos, String buscar, Integer categoriaId) {
        if (buscar != null && !buscar.isBlank()) return repository.findByNombreContainingIgnoreCase(buscar);
        if (categoriaId != null) return repository.findByCategoriaId(categoriaId);
        return soloActivos ? repository.findByActivoTrue() : repository.findAll();
    }
    public Producto obtener(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }
    public Producto guardar(Producto producto) {
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null)
            producto.setCategoria(categorias.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada")));
        return repository.save(producto);
    }
    public Producto crear(Producto producto) { producto.setId(null); return guardar(producto); }
    public Producto actualizar(Integer id, Producto datos) {
        Producto actual = obtener(id);
        datos.setId(actual.getId()); datos.setFechaCreacion(actual.getFechaCreacion());
        return guardar(datos);
    }
    public void eliminar(Integer id) { repository.delete(obtener(id)); }
}
