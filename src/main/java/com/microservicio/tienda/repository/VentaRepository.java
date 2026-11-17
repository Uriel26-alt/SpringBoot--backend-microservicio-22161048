package com.microservicio.tienda.repository;

import com.microservicio.tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstado(Venta.EstadoVenta estado);

    List<Venta> findByClienteNombreContainingIgnoreCase(String clienteNombre);

    List<Venta> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalles WHERE v.id = :id")
    Venta findByIdWithDetalles(Long id);
}
