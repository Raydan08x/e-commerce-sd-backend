package com.sierra_dorada.repository;
import com.sierra_dorada.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findByPedidoId(Integer pedidoId);
}
