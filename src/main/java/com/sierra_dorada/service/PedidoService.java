package com.sierra_dorada.service;

import com.sierra_dorada.exception.RecursoNoEncontradoException;
import com.sierra_dorada.model.DetallePedido;
import com.sierra_dorada.model.MetodoPago;
import com.sierra_dorada.model.Pedido;
import com.sierra_dorada.model.Producto;
import com.sierra_dorada.model.Usuario;
import com.sierra_dorada.repository.MetodoPagoRepository;
import com.sierra_dorada.repository.PedidoRepository;
import com.sierra_dorada.repository.ProductoRepository;
import com.sierra_dorada.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final PedidoRepository pedidos;
    private final UsuarioRepository usuarios;
    private final ProductoRepository productos;
    private final MetodoPagoRepository metodosPago;

    public PedidoService(
            PedidoRepository pedidos,
            UsuarioRepository usuarios,
            ProductoRepository productos,
            MetodoPagoRepository metodosPago) {
        this.pedidos = pedidos;
        this.usuarios = usuarios;
        this.productos = productos;
        this.metodosPago = metodosPago;
    }

    public List<Pedido> listar(Integer usuarioId) {
        return usuarioId == null ? pedidos.findAll() : pedidos.findByUsuarioId(usuarioId);
    }

    public Pedido obtener(Integer id) {
        return pedidos.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado"));
    }

    @Transactional
    public Pedido crear(Pedido entrada) {
        entrada.setId(null);
        preparar(entrada);
        return pedidos.save(entrada);
    }

    @Transactional
    public Pedido actualizar(Integer id, Pedido entrada) {
        Pedido actual = obtener(id);
        entrada.setId(actual.getId());
        entrada.setFechaPedido(actual.getFechaPedido());
        preparar(entrada);
        return pedidos.save(entrada);
    }

    public void eliminar(Integer id) {
        pedidos.delete(obtener(id));
    }

    private void preparar(Pedido pedido) {
        pedido.setUsuario(obtenerUsuario(pedido));
        pedido.setMetodoPago(obtenerMetodoPago(pedido));
        pedido.setTotal(prepararDetalles(pedido));
    }

    private Usuario obtenerUsuario(Pedido pedido) {
        if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        return usuarios.findById(pedido.getUsuario().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }

    private MetodoPago obtenerMetodoPago(Pedido pedido) {
        if (pedido.getMetodoPago() == null || pedido.getMetodoPago().getId() == null) {
            return null;
        }

        return metodosPago.findById(pedido.getMetodoPago().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Método de pago no encontrado"));
    }

    private BigDecimal prepararDetalles(Pedido pedido) {
        List<DetallePedido> detallesEntrada = pedido.getDetalles() == null
                ? List.of()
                : new ArrayList<>(pedido.getDetalles());
        pedido.setDetalles(new ArrayList<>());

        BigDecimal total = BigDecimal.ZERO;
        for (DetallePedido detalle : detallesEntrada) {
            validarDetalle(detalle);
            Producto producto = obtenerProducto(detalle);

            // El precio y el total siempre se calculan con datos de la base, no con valores del cliente.
            detalle.setId(null);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            pedido.agregarDetalle(detalle);
            total = total.add(calcularSubtotal(producto, detalle.getCantidad()));
        }

        return total;
    }

    private void validarDetalle(DetallePedido detalle) {
        boolean productoInvalido = detalle.getProducto() == null
                || detalle.getProducto().getId() == null;
        boolean cantidadInvalida = detalle.getCantidad() == null || detalle.getCantidad() < 1;

        if (productoInvalido || cantidadInvalida) {
            throw new IllegalArgumentException("Cada detalle requiere producto y cantidad válida");
        }
    }

    private Producto obtenerProducto(DetallePedido detalle) {
        Producto producto = productos.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        if (!Boolean.TRUE.equals(producto.getActivo()) || producto.getStock() < detalle.getCantidad()) {
            throw new IllegalArgumentException(
                    "Producto inactivo o sin stock suficiente: " + producto.getNombre());
        }

        return producto;
    }

    private BigDecimal calcularSubtotal(Producto producto, Integer cantidad) {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
