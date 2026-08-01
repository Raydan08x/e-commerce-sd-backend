package com.sierra_dorada.service;

import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.Categoria;
import com.sierra_dorada.model.Producto;
import com.sierra_dorada.repository.CategoriaRepository;
import com.sierra_dorada.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productos;
    private final CategoriaRepository categorias;

    public ProductoService(ProductoRepository productos, CategoriaRepository categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    public List<Producto> listar(boolean soloActivos, String textoBusqueda, Integer categoriaId) {
        if (textoBusqueda != null && !textoBusqueda.isBlank()) {
            return productos.findByNombreContainingIgnoreCase(textoBusqueda);
        }
        if (categoriaId != null) {
            return productos.findByCategoriaId(categoriaId);
        }
        return soloActivos ? productos.findByActivoTrue() : productos.findAll();
    }

    public Producto obtener(Integer id) {
        return productos.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }

    public Producto crear(Producto producto) {
        producto.setId(null);
        return guardar(producto);
    }

    public Producto actualizar(Integer id, Producto datos) {
        Producto actual = obtener(id);
        datos.setId(actual.getId());
        datos.setFechaCreacion(actual.getFechaCreacion());
        return guardar(datos);
    }

    public void eliminar(Integer id) {
        productos.delete(obtener(id));
    }

    private Producto guardar(Producto producto) {
        producto.setCategoria(resolverCategoria(producto));
        return productos.save(producto);
    }

    private Categoria resolverCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            return null;
        }

        return categorias.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
    }
}
