package com.sierra_dorada.service;

import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.*;
import com.sierra_dorada.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PedidoService {
    private final PedidoRepository pedidos;
    private final UsuarioRepository usuarios;
    private final ProductoRepository productos;
    private final MetodoPagoRepository metodosPago;
    public PedidoService(PedidoRepository pedidos, UsuarioRepository usuarios,
                         ProductoRepository productos, MetodoPagoRepository metodosPago) {
        this.pedidos = pedidos; this.usuarios = usuarios;
        this.productos = productos; this.metodosPago = metodosPago;
    }
    public List<Pedido> listar(Integer usuarioId) {
        return usuarioId == null ? pedidos.findAll() : pedidos.findByUsuarioId(usuarioId);
    }
    public Pedido obtener(Integer id) {
        return pedidos.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado"));
    }
    @Transactional
    public Pedido crear(Pedido entrada) { entrada.setId(null); preparar(entrada); return pedidos.save(entrada); }
    @Transactional
    public Pedido actualizar(Integer id, Pedido entrada) {
        Pedido actual = obtener(id);
        entrada.setId(actual.getId()); entrada.setFechaPedido(actual.getFechaPedido());
        preparar(entrada);
        return pedidos.save(entrada);
    }
    public void eliminar(Integer id) { pedidos.delete(obtener(id)); }
    private void preparar(Pedido pedido) {
        if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null)
            throw new IllegalArgumentException("El usuario es obligatorio");
        pedido.setUsuario(usuarios.findById(pedido.getUsuario().getId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado")));
        if (pedido.getMetodoPago() != null && pedido.getMetodoPago().getId() != null)
            pedido.setMetodoPago(metodosPago.findById(pedido.getMetodoPago().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado")));
        List<DetallePedido> entradas = new ArrayList<>(pedido.getDetalles() == null ? List.of() : pedido.getDetalles());
        pedido.setDetalles(new ArrayList<>());
        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : entradas) {
            if (detalle.getProducto() == null || detalle.getProducto().getId() == null || detalle.getCantidad() == null || detalle.getCantidad() < 1)
                throw new IllegalArgumentException("Cada detalle requiere producto y cantidad válida");
            Producto producto = productos.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
            if (!Boolean.TRUE.equals(producto.getActivo()) || producto.getStock() < detalle.getCantidad())
                throw new IllegalArgumentException("Producto inactivo o sin stock suficiente: " + producto.getNombre());
            detalle.setId(null); detalle.setProducto(producto); detalle.setPrecioUnitario(producto.getPrecio());
            pedido.agregarDetalle(detalle);
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }
        pedido.setTotal(total);
    }
}
